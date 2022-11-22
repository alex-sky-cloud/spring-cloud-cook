package com.rabbit.consumer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


/** Чтобы получить поддержку Spring Security WebFlux */
@EnableWebFluxSecurity
/*включает защиту безопасности для методов, которые используются
реактивном стеке*/
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfiguration {

    /***
     * Настраиваем web-security, чтобы разрешить публичные
     * вызовы для всех транзакций. То есть любой запрос в данный сервис, не будет
     * проходить проверку авторизации и аутентификации.
     * permitAll() - позволяет доступ всем клиентам.
      */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange()
                .anyExchange()
                .permitAll();
        return http.build();
    }
}
