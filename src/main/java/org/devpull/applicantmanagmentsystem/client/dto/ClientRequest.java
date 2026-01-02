package org.devpull.applicantmanagmentsystem.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClientRequest(
        @NotBlank @Size(max = 80) String name,
        @NotBlank @Size(max = 80) String lastName,
        @NotNull @Past LocalDate birthDate
) {}
