package com.micro.goal_service.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "realm.client")
public class KeyCloakProperties {
    private String name;
}
