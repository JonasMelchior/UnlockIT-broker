package com.jonas.UnlockIT;

import com.jonas.UnlockIT.entity.locks.Lock;
import com.jonas.UnlockIT.service.ILockService;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Component
public class MyProtocolEncoder implements ProtocolEncoder {


    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        String str = message.toString() + "\0";
        byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
        IoBuffer buffer = IoBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        protocolEncoderOutput.write(buffer);
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
