package com.udp.server.configuration;

import com.udp.server.server.EchoServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.DatagramSocket;
import java.net.SocketException;

@Configuration
public class ConfigurationEchoServer {

    @Value("${app.port}")
    private Integer port;

    @Value("${app.size.buffer}")
    private Integer sizeBuffer;

    @Value("${app.signal.server}")
    private String signalOffServer;

    @Bean
    EchoServer echoServer(){
        DatagramSocket datagramSocket;

        try {
            datagramSocket = new DatagramSocket(this.port);
        } catch (SocketException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        byte[] buffer = new byte [this.sizeBuffer];

        return new EchoServer(datagramSocket, true, buffer, signalOffServer);
    }

    @Bean
    CommandLineRunner init(EchoServer echoServer) {
        return args -> echoServer.run();
    }
}
