package org.devpull.applicantmanagmentsystem.client;

import org.devpull.applicantmanagmentsystem.client.api.ClientController;
import org.devpull.applicantmanagmentsystem.client.repository.MetricsRepository;
import org.devpull.applicantmanagmentsystem.client.service.ClientService;
import org.devpull.applicantmanagmentsystem.security.jwt.*;
import org.devpull.applicantmanagmentsystem.security.web.SecurityErrorWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ClientController.class)
@Import({ClientControllerWebTest.TestSecurityConfig.class, SecurityErrorWriter.class})
@TestPropertySource(properties = {
        "app.security.jwt.secret=TEST_ONLY__0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
        "app.security.jwt.issuer=clients-dev",
        "app.security.jwt.ttl-minutes=60"
})
class ClientControllerWebTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    JwtService jwtService;

    @MockitoBean
    ClientService service;

    @MockitoBean
    MetricsRepository metricsRepository;

    @TestConfiguration
    @EnableWebFluxSecurity
    static class TestSecurityConfig {

        @Bean
        JwtService jwtService(
                @Value("${app.security.jwt.secret}") String secret,
                @Value("${app.security.jwt.issuer}") String issuer
        ) {
            return new JwtService(secret, issuer);
        }

        @Bean
        SecurityWebFilterChain security(ServerHttpSecurity http,
                                        JwtService jwtService,
                                        SecurityErrorWriter errorWriter) {

            var authManager = new JwtAuthenticationManager(jwtService);
            var authFilter = new AuthenticationWebFilter(authManager);

            authFilter.setRequiresAuthenticationMatcher(
                    ServerWebExchangeMatchers.pathMatchers("/api/v1/clients", "/api/v1/clients/**")
            );
            authFilter.setServerAuthenticationConverter(new BearerTokenConverter());
            authFilter.setAuthenticationFailureHandler((webFilterExchange, ex) ->
                    errorWriter.write(webFilterExchange.getExchange(),
                            HttpStatus.UNAUTHORIZED,
                            "Invalid or expired token")
            );

            return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                    .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint((exchange, e) ->
                                    errorWriter.write(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid token"))
                    )
                    .authorizeExchange(ex -> ex
                            .pathMatchers("/api/v1/clients", "/api/v1/clients/**").authenticated()
                            .anyExchange().permitAll()
                    )
                    .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                    .build();
        }
    }

    @Test
    void list_withoutToken_returns401() {
        webTestClient.get().uri("/api/v1/clients")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void list_withToken_returns200() {
        String token = jwtService.mintToken("luis@seek.com", 60);

        when(service.getAll()).thenReturn(Flux.empty());

        webTestClient.get().uri("/api/v1/clients")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();
    }
}
