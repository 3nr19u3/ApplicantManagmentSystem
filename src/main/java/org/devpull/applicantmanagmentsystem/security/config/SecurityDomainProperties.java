package org.devpull.applicantmanagmentsystem.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityDomainProperties(
        String allowedEmailDomain
) {}
