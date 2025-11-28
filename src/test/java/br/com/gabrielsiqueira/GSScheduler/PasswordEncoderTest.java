package br.com.gabrielsiqueira.GSScheduler;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void adminHashMatchesMigration() {
        String hash = "$2a$10$Usl8FYkLyY9co9Hcoibc/O2uL9G5WU8uvACkMuPBfi79Iwt6rLHoS";
        assertThat(encoder.matches("admin123", hash)).isTrue();
    }

    @Test
    void userHashMatchesMigration() {
        String hash = "$2a$10$7QhF8y4um7ncNPML2lXBRubALRcxBmb9ZJ4H7rbhOWCmuUg5iIAWi";
        assertThat(encoder.matches("user123", hash)).isTrue();
    }
}
