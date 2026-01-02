package org.devpull.applicantmanagmentsystem.security.web.helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SeekEmailValidator.class)
public @interface SeekEmail {
    String message() default "email must belong to seek.com";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
