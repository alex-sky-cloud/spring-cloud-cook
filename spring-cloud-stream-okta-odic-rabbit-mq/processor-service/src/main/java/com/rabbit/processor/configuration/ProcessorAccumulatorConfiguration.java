package com.rabbit.processor.configuration;

import com.rabbit.processor.model.AccumulatorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessorAccumulatorConfiguration {

    @Value("${spring.cloud.stream.function.definition}")
    private String definition;

    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * Данная функция потребляет сообщения из одной очереди, обрабатывает
     * их и затем публикует их в другую очередь
     * Integer - получает целые числа из одной очереди
     * AccumulatorMessage - сообщения, которые упаковывают результат
     * обработки и публикуют в брокер
     * this.count.addAndGet(payload) - payload, это данные которые будут
     * потребляться из очереди, затем обрабатываться (в данном случае полученное
     * число будет увеличиваться и упаковываться в контейнер AccumulatorMessage)
     * и публиковаться в другую очередь
     */
    @Bean
    public Function<Integer, AccumulatorMessage> accumulate() {

        log.info("Обработка сообщения в функции accumulate");

        return payload -> new AccumulatorMessage(payload, this.count.addAndGet(payload));
    }
}
