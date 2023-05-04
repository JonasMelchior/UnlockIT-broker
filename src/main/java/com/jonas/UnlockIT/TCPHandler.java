package com.jonas.UnlockIT;

import com.jonas.UnlockIT.controller.LockResponseFuture;
import com.jonas.UnlockIT.entity.locks.Lock;
import com.jonas.UnlockIT.service.ILockService;
import com.jonas.UnlockIT.service.LockService;
import jakarta.annotation.PostConstruct;
import org.apache.mina.core.future.DefaultReadFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class TCPHandler extends IoHandlerAdapter {
    @Autowired
    private LockService lockService;
    private static Map<String, IoSession> sessionMap = new HashMap<>();

    public void sessionCreated(IoSession session) {
        // Add the codec filter to the session
        session.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyProtocolCodecFactory()));
    }

    public static Map<String, IoSession> getSessionMap() {
        return sessionMap;
    }

    public static IoSession getSession(String id) {
        return sessionMap.get(id);
    }

    private static final Map<String, LockResponseFuture> futures = new ConcurrentHashMap<>();

    public static CompletableFuture<String> sendAndReceive(IoSession session, String message) {
        String lockId = (String) session.getAttribute("lockId");
        LockResponseFuture future = new LockResponseFuture(lockId);
        futures.put(lockId, future);
        session.write(message);
        return future;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = message.toString();
        if (str.contains("init")) {
            String[] initData = new String[2];
            initData = str.split(";");
            initData[1] = initData[1].trim();
            sessionMap.put(initData[1], session);
            session.setAttribute("lockId", initData[1]);
        }
        if (str.contains("activate")) {
            String[] data = str.split(":");
            String[] dataParsed = data[1].split(";");
            dataParsed[0] = dataParsed[0].trim();

            //dataParsed[1] = dataParsed[1].trim();
            lockService.save(new Lock(
                    dataParsed[0],
                    dataParsed[1]
            ));
            //For REST endpoints
            String lockId = (String) session.getAttribute("lockId");
            LockResponseFuture future = futures.get(lockId);
            if (future != null) {
                future.complete("Key:" + dataParsed[1]);
                futures.remove(lockId);
            }
        }   
        if (str.contains("challengeInit")) {
            String[] data = str.split(":");
            String[] dataParsed = data[1].split(";");
            dataParsed[0] = dataParsed[0].trim();
            //For REST endpoints
            String lockId = (String) session.getAttribute("lockId");
            LockResponseFuture future = futures.get(lockId);
            try {
                session.setAttribute("nonce_encrypted", AESECBCrypto.encrypt(dataParsed[1].getBytes(StandardCharsets.ISO_8859_1), lockService.findById(dataParsed[0]).getECBKey().getBytes(StandardCharsets.ISO_8859_1)));
                if (future != null) {
                    future.complete("challengeInit received");
                    futures.remove(lockId);
                }
            }
            catch (IllegalBlockSizeException e) {
                future.complete("Illegal Block Size");
                futures.remove(lockId);
            }
        }
        if (str.contains("Granted")) {
            //For REST endpoints
            String lockId = (String) session.getAttribute("lockId");
            LockResponseFuture future = futures.get(lockId);
            if (future != null) {
                future.complete("Access Granted");
                futures.remove(lockId);
            }
        }
        if (str.contains("Denied")) {
            //For REST endpoints
            String lockId = (String) session.getAttribute("lockId");
            LockResponseFuture future = futures.get(lockId);
            if (future != null) {
                future.complete("Access Denied");
                futures.remove(lockId);
            }
        }

        System.out.println("Server received: " + str);
    }
}


