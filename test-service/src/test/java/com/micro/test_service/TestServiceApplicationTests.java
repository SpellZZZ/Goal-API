package com.micro.test_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TestServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getGoalsList() {
        int firstService = 0;
        int secondService = 0;

        for (int i = 0; i < 10; i++) {
            String result = restTemplate.getForObject("http://localhost:8085/lb/test", String.class);
            System.out.println(result);
            if (result.contains("8080")) {
                firstService++;
            } else {
                secondService++;
            }
        }

        System.out.printf("Result: goal-service:8080=%d, goal-service:8081=%d", firstService, secondService);
    }
}
