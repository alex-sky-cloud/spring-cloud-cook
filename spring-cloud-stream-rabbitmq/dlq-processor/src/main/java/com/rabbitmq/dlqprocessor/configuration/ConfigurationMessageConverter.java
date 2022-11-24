package com.rabbitmq.dlqprocessor.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationMessageConverter {

    /**
     * Необходим, чтобы избежать ошибки, во время десериализации
     * сообщений.
     * Например, нужно  определить конфигурацию конвертации JSON,
     * так как если сообщения публикуются в брокер в формате JSON,
     * если именно такой формат сообщений мы указали, когда настраивали в service,
     * который публикует сообщения в брокер
     */
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
