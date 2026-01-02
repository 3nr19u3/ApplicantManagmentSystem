package org.devpull.applicantmanagmentsystem.security.web.helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.devpull.applicantmanagmentsystem.security.config.SecurityDomainProperties;
import org.springframework.stereotype.Component;

@Component
public class SeekEmailValidator implements ConstraintValidator<SeekEmail, String> {

    private final SecurityDomainProperties props;

    public SeekEmailValidator(SecurityDomainProperties props) {
        this.props = props;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotBlank will be charged of null check

        String domain = props.allowedEmailDomain();
        return value.trim().toLowerCase().endsWith("@" + domain);
    }
}
