package com.micro.goal_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final ServletWebServerApplicationContext webServerAppCtxt;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public String test() {
        log.info("Testing " + LocalDateTime.now());
        int port = webServerAppCtxt.getWebServer().getPort();
        return "Message from service port: " + port;
    }
}