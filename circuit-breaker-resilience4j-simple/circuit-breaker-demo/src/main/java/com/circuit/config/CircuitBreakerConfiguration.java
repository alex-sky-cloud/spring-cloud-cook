package com.circuit.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
@Slf4j
public class CircuitBreakerConfiguration {


    /**
     *slidingWindowType() – Эта конфигурация в основном помогает принять решение о том,
     *  как будет работать Прерыватель цепи (circuit breaker).
     * <p>
     * COUNT_BASED - будет учитывать количество обращений к удаленному Service
     * </p>
     * <p>
     * slidingWindowSize() – Эта настройка помогает определить количество вызовов,
     * которые следует учитывать при включении Прерывателя цепи (circuit breaker).
     * </p>
     * <p>
     * slowCallRateThreshold() – Это настраивает пороговое значение скорости медленных вызовов
     * в процентах. Если x-процентов вызовов медленные,
     * Прерыватель цепи (circuit breaker) разомкнется.
     * </p>
     *
     * slowCallDurationThreshold() – Пороговое значение длительности,
     * относительно которого вызовы считаются медленными.
     *
     */
    @Bean
    public CircuitBreaker countCircuitBreaker() {

        int amountRemoteServiceCalls = 10;
        float speedLimitSlowlyAmountCallsInPercent = 65.0f;

        Duration timeDurationsLimitSlowlyCallsInSec = Duration.ofSeconds(3);

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(amountRemoteServiceCalls)
                .slowCallRateThreshold(speedLimitSlowlyAmountCallsInPercent)
                .slowCallDurationThreshold(timeDurationsLimitSlowlyCallsInSec)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

       return circuitBreakerRegistry.circuitBreaker("BooksSearchServiceBasedOnCount");

    }

    /**
     * TIME_BASED скользящее окно(sliding window) Прерывателя цепи (circuit breaker) будет учитывать
     * вызовы удаленного Service, в течение определенного промежутка времени.
     * <p>
     * minimumNumberOfCalls() – Минимальное количество вызовов, перед которым
     * Прерыватель цепи (circuit breaker) может рассчитать коэффициент ошибок.
     * </p>
     *
     * failureRateThreshold()– Это настраивает порог частоты отказов в процентах.
     * Если x процентов вызовов не выполняются, Прерыватель цепи (circuit breaker) размыкается.
     * <p>
     * waitDurationInOpenState() – продолжительность, в течение которой Прерыватель цепи (circuit breaker) должен оставаться в разомкнутом (OPEN)
     * состоянии перед переходом в полу-разомкнутое (OPEN-HALF) состоянии, чтобы попробовать
     * передать несколько вызовов для проверки работоспособности удаленного Service
     * Значение по умолчанию — 60 секунд.
     * </p>
     */
    @Bean
    public CircuitBreaker timeCircuitBreaker() {

        int minAmountCalls = 3;
        int amountRemoteServiceCalls = 10;
        float rateThresholdFailureInPercent = 70.0f;

        int seconds = 10;

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .minimumNumberOfCalls(minAmountCalls)
                .slidingWindowSize(amountRemoteServiceCalls)
                .failureRateThreshold(rateThresholdFailureInPercent)
                .waitDurationInOpenState(Duration.ofSeconds(seconds))
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        return circuitBreakerRegistry.circuitBreaker("BookSearchServiceBasedOnTime");
    }
}
