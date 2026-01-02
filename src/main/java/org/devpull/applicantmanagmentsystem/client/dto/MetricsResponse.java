package org.devpull.applicantmanagmentsystem.client.dto;

import java.math.BigDecimal;

public record MetricsResponse(
        Long quantity,
        BigDecimal average,
        BigDecimal standardDeviation
) {}
