package com.rabbit.consumer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ResourceStream {

    private final Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

    /**
     * Помечен как Deprecated и предлагает к замене
     * Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
     */
    private final EmitterProcessor<String> streamProcessor = EmitterProcessor.create();

    /**
     * MediaType.TEXT_EVENT_STREAM_VALUE - Это указывает на то,
     * что данные отправляются в форме событий, отправляемых сервером (Server Sent Events),
     * которые кратко называются SSE.
     * Server-Sent-Events, или сокращенно SSE, - это стандарт HTTP,
     * который позволяет веб-приложению обрабатывать однонаправленный
     * поток событий и получать обновления всякий раз, когда сервер отправляет данные.
     *
     * @return возвращается поток значений типа String
     */
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getSee() {

        return this.streamProcessor;
    }

    @Bean
    public Consumer<Flux<String>> receiveSse() {
        return recordFlux ->
                recordFlux
                        .doOnNext(this.streamProcessor::onNext)
                        .doOnNext(value -> log.info("*" + value))
                        .subscribe();
    }
}
