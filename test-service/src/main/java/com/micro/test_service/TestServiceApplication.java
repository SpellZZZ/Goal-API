package com.micro.test_service;

import com.micro.test_service.config.GoalClientConfiguration;
import com.micro.test_service.config.LoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration = GoalClientConfiguration.class)
@LoadBalancerClients(defaultConfiguration = LoadBalancerConfiguration.class)
public class TestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }

}
