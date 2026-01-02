package org.devpull.applicantmanagmentsystem.common.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public final class AgeUtils {

    private AgeUtils() {
        // utility class
    }

    /**
     * Calculates age in full years from date of birth.
     *
     * @param birthDate date of birth (not null, not future)
     * @return age in years
     * @throws IllegalArgumentException if date is future
     */
    public static int calculateAge(LocalDate birthDate) {
        Objects.requireNonNull(birthDate, "birthDate must not be null");

        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }

        return Period.between(birthDate, today).getYears();
    }
}
