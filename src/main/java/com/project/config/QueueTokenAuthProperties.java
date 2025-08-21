package com.project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "queue.auth")
@Getter @Setter
public class QueueTokenAuthProperties {
    private boolean enabled = false;
}
