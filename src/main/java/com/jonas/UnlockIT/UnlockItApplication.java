package com.jonas.UnlockIT;

import com.jonas.UnlockIT.entity.locks.Lock;
import com.jonas.UnlockIT.repository.LockRepository;
import com.jonas.UnlockIT.service.ILockService;
import com.jonas.UnlockIT.service.LockService;
import jakarta.annotation.PostConstruct;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Component
public class UnlockItApplication {

	public static TCPHandler tcpHandler;
	IoAcceptor acceptor = new NioSocketAcceptor();

	@Autowired
	public void setTcpHandler(TCPHandler tcpHandler) {
		UnlockItApplication.tcpHandler = tcpHandler;
	}

	public static void main(String[] args) throws IOException {
		SpringApplication.run(UnlockItApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() throws IOException {
		acceptor.setHandler(tcpHandler);

		acceptor.bind(new InetSocketAddress(13370));

		System.out.println("Server running on port: 13370.");

		setTcpHandler(new TCPHandler());
	}
}
