package org.devpull.applicantmanagmentsystem.security.web;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.devpull.applicantmanagmentsystem.security.jwt.JwtService;
import org.devpull.applicantmanagmentsystem.security.web.helper.TokenRequest;
import org.devpull.applicantmanagmentsystem.security.web.helper.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Profile("dev")
@RestController
@RequestMapping("/auth")
@SecurityRequirements
public class DevAuthController {

    private final JwtService jwtService;
    private final long ttlMinutes;

    public DevAuthController(JwtService jwtService,
                             @Value("${app.security.jwt.ttl-minutes:60}") long ttlMinutes) {
        this.jwtService = jwtService;
        this.ttlMinutes = ttlMinutes;
    }

    @PostMapping("/token")
    public Mono<TokenResponse> token(@Valid @RequestBody TokenRequest req) {
        String email = req.email().trim().toLowerCase();

        String token = jwtService.mintToken(email, ttlMinutes);
        return Mono.just(new TokenResponse(token));
    }
}
