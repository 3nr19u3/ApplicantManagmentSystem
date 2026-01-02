package org.devpull.applicantmanagmentsystem.docs.openapi;

import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiCustomization {

    @Bean
    OpenApiCustomizer globalCustomizer() {
        return (OpenAPI api) -> {
            api.setTags(List.of(
                    new Tag().name("Clients").description("Operaciones de clientes"),
                    new Tag().name("Health").description("Salud del servicio")
            ));

            // Servers (opcional)
            // api.setServers(List.of(new Server().url("http://localhost:8080").description("local")));
        };
    }
}
