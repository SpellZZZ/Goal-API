package com.micro.test_service.controller;

import com.micro.test_service.client.GoalClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/lb/test")
public class TestController {

    @Autowired
    private GoalClient testClient;

    @GetMapping
    @Retry(name = "default", fallbackMethod = "FallbackHandler.RetryResponse")
    @CircuitBreaker(name = "default", fallbackMethod = "FallbackHandler.CircuitBreakerResponse")
    @RateLimiter(name="default", fallbackMethod = "FallbackHandler.RateLimiterResponse")
    public String test() {
        log.info("Getting test " + LocalDateTime.now());
        return testClient.test();
    }
}
