package com.circuit.manager;

import com.circuit.dto.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class BookCircuitBreakerManager {

    private final RestTemplate restTemplate;

    private static final String URI_TO_REMOTE_SERVICE = "http://localhost:8089/v1/library/books";

    /**
     * отправляем запрос у удаленному сервису, от имени сервера с
     */
    public List<Book> getAllBooksFromLibrary() {
        
        ResponseEntity<List<Book>> responseEntity;

        /*замер скорости обработки запроса*/
        long startTime = System.currentTimeMillis();
        log.info("Start time = {}", startTime);

        try {
            responseEntity = restTemplate
                    .exchange(
                            buildUrl(),
                            HttpMethod.GET,
                            buildHttpEntity(),
                            new ParameterizedTypeReference<>() {
                            }
                    );

            if (responseEntity.hasBody()) {

                Thread.sleep(3000);
                log.info("Общее время, затраченное на извлечение " +
                                "результатов от удаленного сервиса Book = {}",
                        System.currentTimeMillis() - startTime);

                return responseEntity.getBody();
            }
        } catch (URISyntaxException e) {
            log.error("URI к удаленному сервису Book имеет неправильный syntax", e);
        } catch (InterruptedException e) {
            log.info("Interrupted! + " + e.getLocalizedMessage(), Level.WARN);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        log.info("Результат не найден, возвращен пустой list");
        return new ArrayList<>();
    }

    private HttpEntity<Object> buildHttpEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(httpHeaders);
    }

    /**
     * метод преобразует адрес удаленного сервиса в String-формате
     * в тип URI
     */
    private URI buildUrl() throws URISyntaxException {
        return new URI(URI_TO_REMOTE_SERVICE);
    }
}
