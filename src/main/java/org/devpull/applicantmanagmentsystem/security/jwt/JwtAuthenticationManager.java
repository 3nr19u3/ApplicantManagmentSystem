package org.devpull.applicantmanagmentsystem.security.jwt;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        try {
            var claims = jwtService.parse(token).getPayload();
            String email = claims.getSubject();

            if (email == null || email.isBlank()) {
                return Mono.error(new BadCredentialsException("Invalid token subject"));
            }

            // Sin roles/authorities
            return Mono.just(new UsernamePasswordAuthenticationToken(email, token, List.of()));
        } catch (Exception e) {
            return Mono.error(new BadCredentialsException("Invalid or expired token", e));
        }
    }

}
