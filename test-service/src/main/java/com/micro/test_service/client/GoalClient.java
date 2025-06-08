package com.micro.test_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "goal-service")
public interface GoalClient {
    @GetMapping("/api/test")
    public String test();
}
