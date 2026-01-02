package org.devpull.applicantmanagmentsystem.client.repository;

import org.devpull.applicantmanagmentsystem.client.dto.MetricsResponse;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public class MetricsRepository {

    private final DatabaseClient db;

    public MetricsRepository(DatabaseClient db) {
        this.db = db;
    }

    public Mono<MetricsResponse> metrics() {
        return db.sql("""
        SELECT
          COUNT(*) AS quantity,
          AVG(age) AS average,
          STDDEV_POP(age) AS standardDeviation
        FROM clients
    """)
                .map((row, meta) -> new MetricsResponse(
                        row.get("quantity", Long.class),
                        getBigDecimal(row, "average"),
                        getBigDecimal(row, "standardDeviation")
                ))
                .one();
    }

    private BigDecimal getBigDecimal(io.r2dbc.spi.Row row, String col) {
        Object v = row.get(col);
        return switch (v) {
            case null -> null;
            case BigDecimal bd -> bd;
            case Number n -> BigDecimal.valueOf(n.doubleValue());
            default -> new BigDecimal(v.toString());
        };
    }
}

