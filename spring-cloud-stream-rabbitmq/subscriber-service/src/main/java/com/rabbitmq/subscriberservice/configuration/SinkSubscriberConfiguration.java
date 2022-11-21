package com.rabbitmq.subscriberservice.configuration;


import com.rabbitmq.subscriberservice.model.MessageDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.function.Consumer;


@Configuration
@Slf4j
public class SinkSubscriberConfiguration {

    @Bean
    public Consumer<MessageDto> subscribe() {

        return messageDto -> {
            if(messageDto.getMessage().contains("wrong")) {
                log.error("Spam message: " + messageDto);
                throw new MessageException("Error spam!!!");
            }

            log.info("receivedMessage: " + messageDto);
        };
    }
}
