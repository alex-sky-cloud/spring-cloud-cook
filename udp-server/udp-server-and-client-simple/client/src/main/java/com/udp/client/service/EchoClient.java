package com.udp.client.service;

public interface EchoClient {

    /**
     * Может выбросить IOException
     */
    String sendEcho(String msg);

    void closeSocket();
}
