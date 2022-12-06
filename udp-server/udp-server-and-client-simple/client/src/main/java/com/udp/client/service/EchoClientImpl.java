package com.udp.client.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import static com.udp.client.utils.ConvertUtils.convertByteArrToString;
import static com.udp.client.utils.ConvertUtils.convertStringToByteArr;

@Component
@Slf4j
public class EchoClientImpl implements EchoClient{

    private final DatagramSocket socket;
    private final InetAddress address;

    @Value("${udp.server.port}")
    private Integer portUdpSever;

    /**
     * InetAddress.getByName("localhost") - значение в метод getByName(),
     * нужно поставлять из application.yml. Поэтому здесь можно переделать
     * кода на взаимодействие c
     * Properties, используя {@link org.springframework.boot.context.properties.ConfigurationProperties}
     */
    @Autowired
    public EchoClientImpl() throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(300);
        this.address = InetAddress.getByName("localhost");
    }

    @Override
    public String sendEcho(String msg) {

        byte[] buffer = convertStringToByteArr(msg);

        int lengthMessage = buffer.length;

        DatagramPacket packet = new DatagramPacket(
                buffer,
                lengthMessage,
                address,
                this.portUdpSever);

        /*На методе send() поток блокируется, пока не будет получен ответ от UDP-server*/
        try {
            log.info("Клиент UDP посылает сообщение к UDP-server.");
            socket.send(packet);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        /*Формируем контейнер, в который будет сохранен ответ, полученный от сервера UDP*/
        packet = new DatagramPacket(buffer, lengthMessage);


        /*пробуем получить ответ от UDP-server.
        * Если заданное выше, значение таймаута от сервера будет превышено,
        * тогда обработаем IOException*/
        try {
            socket.receive(packet);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        /*один вариант преобразования полученного ответа*/
        byte[] dataFromUdpServer = packet.getData();
        String response = convertByteArrToString(dataFromUdpServer);
        log.info("Клиент UDP получил ответ от сервера:. " + response);

        /*2-й вариант преобразования полученного ответа*/
        return new String(dataFromUdpServer, 0, packet.getLength());
    }

    @Override
    public void closeSocket() {
        socket.close();
    }
}
