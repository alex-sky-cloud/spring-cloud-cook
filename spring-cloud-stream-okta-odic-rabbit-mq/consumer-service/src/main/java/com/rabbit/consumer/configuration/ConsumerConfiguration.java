package com.rabbit.consumer.configuration;

import com.rabbit.consumer.model.AccumulatorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class ConsumerConfiguration {

    @Value("${spring.cloud.stream.function.definition}")
    private String definition;

    @Bean
    public Consumer<AccumulatorMessage> receive() {

        return payload -> {
            log.info("Получение данных из очереди. Обрабатывает метод " + definition + " - данные -" + payload.toString());
        };
    }
}
