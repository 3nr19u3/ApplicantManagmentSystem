package org.devpull.applicantmanagmentsystem.security.web;


import org.devpull.applicantmanagmentsystem.common.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class SecurityErrorWriter {

    public Mono<Void> write(ServerWebExchange exchange, HttpStatus status, String message) {
        var response = exchange.getResponse();
        if (response.isCommitted()) return response.setComplete();

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String path = exchange.getRequest().getPath().value();
        String requestId = resolveRequestId(exchange.getRequest());

        var body = ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                requestId
        );

        var json = """
            {
              "timestamp":"%s",
              "status":%d,
              "error":"%s",
              "message":"%s",
              "path":"%s",
              "requestId":"%s"
            }
            """.formatted(
                body.timestamp(),
                body.status(),
                escape(body.error()),
                escape(body.message()),
                escape(body.path()),
                escape(body.requestId())
        ).trim();

        var buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    private String resolveRequestId(ServerHttpRequest request) {
        String rid = request.getHeaders().getFirst("X-Request-Id");
        return rid != null ? rid : request.getId();
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}