package org.devpull.applicantmanagmentsystem.common.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Order(-2) // ensure high precedence
public class GlobalErrorHandler implements WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();
        if (response.isCommitted()) return Mono.error(ex);

        response.getHeaders().setContentType(APPLICATION_JSON);

        HttpStatus status;
        String message;

        // 400
        if (ex instanceof WebExchangeBindException bindEx) {
            status = HttpStatus.BAD_REQUEST;
            message = bindEx.getFieldErrors().stream()
                    .map(this::formatFieldError)
                    .collect(Collectors.joining("; "));
        }
        // Custom errors launch by you with status code explicit
        else if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            message = rse.getReason() != null ? rse.getReason() : rse.getMessage();
        }
        // error unique keys
        else if (ex instanceof DuplicateKeyException) {
            status = HttpStatus.CONFLICT;
            message = "Resource already exists";
        }
        // fallback
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Unexpected error";
            log.error("Unhandled exception", ex);
        }

        response.setStatusCode(status);

        String path = exchange.getRequest().getPath().value();
        String requestId = resolveRequestId(exchange.getRequest());

        var body = ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                requestId
        );

        var buffer = response.bufferFactory().wrap(Json.write(body).getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    private String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + (fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid");
    }

    private String resolveRequestId(ServerHttpRequest request) {
        // to future, we can use header
        String rid = request.getHeaders().getFirst("X-Request-Id");
        return rid != null ? rid : request.getId();
    }

    /**
     * Minimal utility for serializing without putting ObjectMapper in the handler.
     * If you prefer, I can give you a version with ObjectMapper injected.
     */
    static class Json {
        static String write(ApiError e) {
            // Enough if you need full response you should use ObjectMapper.
            return """
                {
                  "timestamp":"%s",
                  "status":%d,
                  "error":"%s",
                  "message":"%s",
                  "path":"%s",
                  "requestId":"%s"
                }
                """.formatted(
                    e.timestamp(), e.status(), safe(e.error()), safe(e.message()), safe(e.path()), safe(e.requestId())
            ).trim();
        }

        static String safe(String s) {
            return s == null ? "" : s.replace("\"", "\\\"");
        }
    }

}

