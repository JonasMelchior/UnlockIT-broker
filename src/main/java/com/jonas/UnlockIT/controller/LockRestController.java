package com.jonas.UnlockIT.controller;

import com.jonas.UnlockIT.TCPHandler;
import com.jonas.UnlockIT.UnlockItApplication;
import com.jonas.UnlockIT.entity.locks.Lock;
import com.jonas.UnlockIT.service.ILockService;
import org.apache.mina.core.future.DefaultReadFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class LockRestController {

    @Autowired
    ILockService lockService;

    @GetMapping("/start")
    public CompletableFuture<String> sendMessageToLock(@RequestParam(value = "id") String lockId) throws IOException {

        IoSession session = TCPHandler.getSession(lockId);
        String message = lockId + ";challengeInit";

        if (session != null && session.isConnected()) {
            return TCPHandler.sendAndReceive(session, message);
        } else {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("TCP server is not available."));
            return future;
        }
    }

    @GetMapping("/respond")
    public CompletableFuture<String> respondToChallenge(@RequestParam(value = "id") String lockId) throws IOException {
        IoSession session = TCPHandler.getSession(lockId);
        byte[] nonceEncrypted = (byte[]) session.getAttribute("nonce_encrypted");
        String message = lockId + ";challengeResponse:" + new String(nonceEncrypted, StandardCharsets.ISO_8859_1);

        if (session.isConnected()) {
            return TCPHandler.sendAndReceive(session, message);
        } else {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("TCP server is not available."));
            return future;
        }

    }

    @GetMapping("/activate")
    public CompletableFuture<String> activateLock(@RequestParam(value = "id") String lockId) throws IOException {
        IoSession session = TCPHandler.getSession(lockId);
        String message = lockId + ";activate";
        if (session.isConnected()) {
            return TCPHandler.sendAndReceive(session, message);
        } else {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("TCP server is not available."));
            return future;
        }
    }

    @GetMapping("/getKey")
    public String getKey(@RequestParam(value = "id") String lockId) throws IOException {
        return lockService.findById(lockId).getECBKey();
    }

    @GetMapping("/list_locks")
    public String listLocks() {
        return TCPHandler.getSessionMap().keySet().toString();
    }

    @Async
    public CompletableFuture<String> getResponseFuture(IoSession session) {
        DefaultReadFuture readFuture = new DefaultReadFuture(session);

        CompletableFuture<String> future = new CompletableFuture<>();
        readFuture.addListener(f -> {
            if (f.isDone()) {
                CompletableFuture.completedFuture(readFuture.getMessage());
            } else {
                future.completeExceptionally(new Exception("Failed to get response from lock."));
            }
        });

        return future;
    }
}
