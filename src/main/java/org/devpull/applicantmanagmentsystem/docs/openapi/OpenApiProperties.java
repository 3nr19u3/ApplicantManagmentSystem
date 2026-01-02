package org.devpull.applicantmanagmentsystem.docs.openapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.openapi")
public record OpenApiProperties(
        String title,
        String description,
        String version,
        String termsOfService,
        Contact contact,
        License license,
        Ui ui
) {
    public record Contact(String name, String url, String email) {}
    public record License(String name, String url) {}
    public record Ui(String path, boolean enabled) {}
}
