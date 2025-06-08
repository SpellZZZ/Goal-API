package com.micro.test_service.handler;

import org.springframework.stereotype.Component;

@Component
public class FallbackHandler {

    public String RetryResponse(Throwable throwable) {
        return "(Retry): " + throwable.getMessage();
    }

    public String CircuitBreakerResponse(Throwable throwable) {
        return "(CircuitBreaker): " + throwable.getMessage();
    }

    public String RateLimiterResponse(Throwable throwable) {
        return "(RateLimiter): " + throwable.getMessage();
    }
}
