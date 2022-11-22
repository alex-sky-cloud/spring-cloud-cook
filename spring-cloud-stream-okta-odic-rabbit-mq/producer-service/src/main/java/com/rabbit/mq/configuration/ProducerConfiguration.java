package com.rabbit.mq.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProducerConfiguration {

    @Value("${spring.cloud.stream.function.definition}")
    private String definition;

    private final Random random = new Random();

    @Bean
    public Supplier<Integer> send() {
        log.info("Работает функция : " + definition);

        return () -> random.nextInt(100);
    }
}