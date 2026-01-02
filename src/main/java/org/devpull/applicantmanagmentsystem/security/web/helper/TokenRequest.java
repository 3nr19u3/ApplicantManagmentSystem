package org.devpull.applicantmanagmentsystem.security.web.helper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        @SeekEmail
        String email
) {}
