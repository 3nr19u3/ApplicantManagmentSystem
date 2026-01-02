package org.devpull.applicantmanagmentsystem.security.config;

import org.devpull.applicantmanagmentsystem.security.jwt.BearerTokenConverter;
import org.devpull.applicantmanagmentsystem.security.jwt.JwtAuthenticationManager;
import org.devpull.applicantmanagmentsystem.security.jwt.JwtService;
import org.devpull.applicantmanagmentsystem.security.web.SecurityErrorWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    JwtService jwtService(SecurityProperties props) {
        return new JwtService(props.secret(), props.issuer());
    }

    @Bean
    SecurityWebFilterChain security(ServerHttpSecurity http,
                                    JwtService jwtService,
                                    SecurityErrorWriter errorWriter) {

        var authManager = new JwtAuthenticationManager(jwtService);
        var authFilter = new AuthenticationWebFilter(authManager);

        // Auth paths
        authFilter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers("/api/v1/clients/**")
        );

        // Get the Bearer token form header Authorization
        authFilter.setServerAuthenticationConverter(new BearerTokenConverter());

        // Token inválidate/corrupt/expiraded => 401 + JSON
        authFilter.setAuthenticationFailureHandler((webFilterExchange, ex) ->
                errorWriter.write(
                        webFilterExchange.getExchange(),
                        HttpStatus.UNAUTHORIZED,
                        "Invalid or expired token"
                )
        );

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Handler when token dirty or missing = 401 + JSON
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, e) ->
                                errorWriter.write(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid token")
                        )
                        .accessDeniedHandler((exchange, e) ->
                                errorWriter.write(exchange, HttpStatus.FORBIDDEN, "Insufficient permissions")
                        )
                )

                .authorizeExchange(ex -> ex
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/actuator/health").permitAll()
                        .pathMatchers("/api/v1/clients/**").authenticated()
                        .anyExchange().permitAll()
                )

                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
