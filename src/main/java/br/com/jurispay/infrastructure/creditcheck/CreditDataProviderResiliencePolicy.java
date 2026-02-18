package br.com.jurispay.infrastructure.creditcheck;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
public class CreditDataProviderResiliencePolicy {

    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public CreditDataProviderResiliencePolicy(
            @Value("${app.credit-check.provider.retry.max-attempts:2}") int maxAttempts,
            @Value("${app.credit-check.provider.retry.wait-ms:200}") long waitMs,
            @Value("${app.credit-check.provider.circuit-breaker.sliding-window-size:20}") int slidingWindowSize,
            @Value("${app.credit-check.provider.circuit-breaker.failure-rate-threshold:50}") float failureRateThreshold,
            @Value("${app.credit-check.provider.circuit-breaker.open-state-wait-seconds:20}") long openStateWaitSeconds) {

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(Duration.ofMillis(waitMs))
                .retryExceptions(Exception.class)
                .build();

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(slidingWindowSize)
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofSeconds(openStateWaitSeconds))
                .build();

        this.retry = Retry.of("credit-data-provider", retryConfig);
        this.circuitBreaker = CircuitBreaker.of("credit-data-provider", circuitBreakerConfig);
    }

    public <T> T execute(Supplier<T> supplier) {
        Supplier<T> decorated = CircuitBreaker.decorateSupplier(circuitBreaker, supplier);
        decorated = Retry.decorateSupplier(retry, decorated);
        return decorated.get();
    }
}
