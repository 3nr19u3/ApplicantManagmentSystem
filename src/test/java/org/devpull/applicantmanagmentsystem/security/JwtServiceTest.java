package org.devpull.applicantmanagmentsystem.security;

import org.devpull.applicantmanagmentsystem.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtServiceTest {

    private static final String SECRET =
            "TEST_ONLY__0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
    private static final String ISSUER = "clients-dev";

    @Test
    void mint_and_parse_ok() {
        var jwt = new JwtService(SECRET, ISSUER);

        String token = jwt.mintToken("luis@devpull.com", 60);

        var claims = jwt.parse(token).getPayload();
        assertThat(claims.getSubject()).isEqualTo("luis@devpull.com");
        assertThat(claims.getIssuer()).isEqualTo("clients-dev");
    }

    @Test
    void parse_invalid_token_throws() {
        var jwt = new JwtService(SECRET, ISSUER);
        assertThatThrownBy(() -> jwt.parse("xxx.yyy.zzz"))
                .isInstanceOf(Exception.class);
    }

}
