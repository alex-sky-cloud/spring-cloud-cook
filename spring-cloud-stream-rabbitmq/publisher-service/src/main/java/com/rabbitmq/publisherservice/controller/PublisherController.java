package com.rabbitmq.publisherservice.controller;

import com.rabbitmq.publisherservice.model.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class PublisherController {

    private final static AtomicLong counter = new AtomicLong(1);

    /**позволяет нам отправлять данные в выходную привязку(binding)*/
    private final StreamBridge streamBridge;

    /**
     * В момент вызова данного endpoint, произойдет
     * вызов {@link StreamBridge#send(String, Object)}
     * В этот метод можно передать одна или несколько имен привязок.
     * То есть мы формируем сообщение и можем отправить его в выходной канал,
     * откуда это сообщение может попасть в одну или сразу в несколько
     * определенных очередей, которые подписаны на получение данного
     * сообщения.
     */
    @GetMapping(value = "/send")
    public void sendMessage() {

        String patternMessage = "Send message from publisher: ";
        String bindingName = "send-out-0";

        performStreamMessages(patternMessage, bindingName);
    }



    @GetMapping(value = "/send/spam")
    public MessageDto sendSpamMessage() {

        String patternMessage = "I'm sorry, but wrong you: ";
        String bindingName = "send-out-0";

        performStreamMessages(patternMessage, bindingName);

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(patternMessage + " - not acceptable");

        return messageDto;
    }

    private void performStreamMessages(String patternMessage, String bindingName){

        IntStream.range(0, 10)
                .forEach(i -> {
                    MessageDto message =
                            new MessageDto(patternMessage + counter.getAndIncrement());
                    Message<MessageDto> dtoMessage = MessageBuilder
                            .withPayload(message)
                            .build();
                      /*send-out-0 - имя привязки данного Publisher,
                         которое определили в application.yml*/
                    streamBridge.send(bindingName, dtoMessage);
                });
    }
}
