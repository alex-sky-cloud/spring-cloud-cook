package com.udp.server.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.udp.server.utils.ConvertUtils.convertByteArrToString;
import static com.udp.server.utils.ConvertUtils.convertStringToByteArr;

@AllArgsConstructor
@Slf4j
public class EchoServer {

    private final DatagramSocket socket;

    private final Boolean running;

    private byte[] buffer;

    private final String signalCloseServer;

    public void run() {

        boolean isRunning = this.running;

        while (isRunning) {

            DatagramPacket packet
                    = new DatagramPacket(this.buffer, this.buffer.length);

            /*Поток блокируется на методе receive(packet),
            * пока не будет получен запрос от клиента*/
            try {
                socket.receive(packet);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
                isRunning = false;
            }

            /*Как только придет запрос от клиента, то данные в запросе,
            * будут сохранены в буфер массива байт типа DatagramPacket.
            * Тогда из этой метаинформации мы получаем адрес клиента и порт клиента,
            * которому нужно будет отправить ответ.*/
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            /*Также мы получаем полезные данные, которые пришли на сервер для обработки*/
            String received
                    = new String(packet.getData(), 0, packet.getLength());

            log.info("Получен запрос от клиента - IP-address: " + address + ": port. " +
                    "Сообщение :" + received);

            /*проверяем, что строка, которая пришла в запросе, указывает на остановку
             * сервера. Если это так, то закрываем соединения сервера.*/
            boolean isOffServer = received.equals(this.signalCloseServer);

            byte[] response = new byte[this.buffer.length];

            if(!isOffServer){
                String responseToClient = "Hello " + received + ".";

                response = convertStringToByteArr(responseToClient);

                String validate = convertByteArrToString(response);
                log.info("Клиенту отправляется сообщение " + validate);
            }



            /*Здесь мы данные которые получим, обрабатываем, чтобы отправить
            * по указанному адресу и порту клиента, от которого данные пришли*/
            packet = new DatagramPacket(response, response.length, address, port);



            /*если пришло сообщение на закрытие сервера, мы меняем
            * флаг isRunning, на false, continue Указывает на то, что нужно не выполнять
            * код далее, а вернуться в начало цикла while, в проверку условия.*/
            if (isOffServer) {
                isRunning = false;
                continue;
            }

            /* Отправляем пакет клиенту
            если во время отправки сообщения мы получим exception, тогда
            * мы также меняем флаг isRunning, на false*/
            try {
                socket.send(packet);
            } catch (IOException e) {
                isRunning = false;
                log.error(e.getLocalizedMessage());
            }
        }
        log.info("Сервер завершает работу !!!");
        socket.close();
    }
}