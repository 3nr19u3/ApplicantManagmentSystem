package org.devpull.applicantmanagmentsystem.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public record SecurityProperties(
        String secret,
        String issuer,
        long ttlMinutes
) {}
