package org.devpull.applicantmanagmentsystem.docs.openapi;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiGroupsConfig {

    @Bean
    GroupedOpenApi clientsApi() {
        return GroupedOpenApi.builder()
                .group("clients")
                .pathsToMatch("/api/v1/clients/**")
                .build();
    }

    @Bean
    GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/v1/auth/**", "/auth/**")
                .build();
    }

    @Bean
    GroupedOpenApi everything() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/api/**")
                .build();
    }
}
