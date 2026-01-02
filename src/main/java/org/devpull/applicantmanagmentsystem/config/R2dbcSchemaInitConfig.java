package org.devpull.applicantmanagmentsystem.config;


import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@Profile("dev")
public class R2dbcSchemaInitConfig {

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        initializer.setDatabasePopulator(
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql"))
        );

        return initializer;
    }
}
