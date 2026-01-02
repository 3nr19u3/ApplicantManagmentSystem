package org.devpull.applicantmanagmentsystem.common.error;

import java.time.OffsetDateTime;

public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String requestId
) {
    public static ApiError of(int status, String error, String message, String path, String requestId) {
        return new ApiError(OffsetDateTime.now(), status, error, message, path, requestId);
    }
}
