package org.devpull.applicantmanagmentsystem.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityDomainProperties.class)
public class PropertiesConfig {
}
