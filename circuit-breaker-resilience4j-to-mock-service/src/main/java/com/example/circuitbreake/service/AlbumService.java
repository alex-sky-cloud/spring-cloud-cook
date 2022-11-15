package com.example.circuitbreake.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlbumService {

    private final CircuitBreakerFactory circuitBreakerFactory;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAlbumList() {

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        String url = "https://jsonplaceholder.typicode.com/albums";

        /*Метод run() принимает 2 аргумента:
         * restTemplate.getForObject(url, String.class) - этот аргумент,
         * это запрос к удаленному сервису
         * throwable -> getDefaultAlbumList() - это 2-й аргумент,
         * если произойдет ошибка, то есть удаленный сервис
         * не отвечает, в этом случае, circuit breaker разомкнет цепь
         * и выдаст в качестве результата,
         * данные представленные методом getDefaultAlbumList()
         * */
        return circuitBreaker
                .run(() ->
                                restTemplate.getForObject(url, String.class),
                        throwable -> getDefaultAlbumList()
                );
    }

    /*заглушка, которая читает данные из предоставленного JSON-файла и преобразует их в строку*/
    private String getDefaultAlbumList() {

        URI uri = buildUri();

        Path pathToMockResponse = Paths.get(uri);

        byte[] bytes = readResponseInputStream(pathToMockResponse);

        try {
            return new String(Files.readAllBytes(pathToMockResponse)
            );
        } catch (Exception e) {
            log.error("error occurred while reading the file", e);
        }

        return new String(bytes);
    }

    private byte[] readResponseInputStream(Path pathToMockResponse) {

        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(pathToMockResponse);
        } catch (IOException e) {
            log.error("error occurred while reading the file", e);
        }

        return bytes;
    }


    private URI buildUri() {
        URI uri = null;
        try {
            uri = Objects.requireNonNull(
                    getClass()
                            .getClassLoader()
                            .getResource("fallback-album-list.json")).toURI();
        } catch (URISyntaxException e) {
            log.error("error occurred while building an URI", e);
        }

        return uri;
    }

}
