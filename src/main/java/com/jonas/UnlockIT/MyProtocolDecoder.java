package com.jonas.UnlockIT;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
@Component
public class MyProtocolDecoder implements ProtocolDecoder {
    private Charset charset = StandardCharsets.ISO_8859_1;

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        String str = ioBuffer.getString(charset.newDecoder());
        protocolDecoderOutput.write(str);
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    }

    public void dispose(IoSession session) throws Exception {
    }
}