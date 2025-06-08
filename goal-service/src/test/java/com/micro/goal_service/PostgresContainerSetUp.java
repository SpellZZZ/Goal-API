package com.micro.goal_service;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@ActiveProfiles("testcontainers-liquibase")
public class PostgresContainerSetUp {

    private static String POSTGRES_VERSION = "postgres:latest";
    private static String USER_NAME = "test";
    private static String USER_PASSWORD = "test";
    private static String DATABASE_NAME = "test";

    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_VERSION)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USER_NAME)
            .withPassword(USER_PASSWORD);

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @BeforeAll
    public static void setUp() {
        postgreSQLContainer.setWaitStrategy(
                new LogMessageWaitStrategy()
                        .withRegEx(".*database system is ready to accept connections.*\\s")
                        .withTimes(1)
                        .withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS))
        );
        postgreSQLContainer.start();
    }
}