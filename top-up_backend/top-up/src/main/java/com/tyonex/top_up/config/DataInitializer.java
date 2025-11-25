package com.tyonex.top_up.config;

import com.tyonex.top_up.entity.User;
import com.tyonex.top_up.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Admin kullanıcı oluştur
        if (!userRepository.existsByEmail("admin@topup.com")) {
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@topup.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .status(User.UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
            log.info("Admin kullanıcı oluşturuldu: admin@topup.com / admin123");
        } else {
            log.info("Admin kullanıcı zaten mevcut: admin@topup.com");
        }

        // Test kullanıcısı oluştur (opsiyonel)
        if (!userRepository.existsByEmail("test@example.com")) {
            User testUser = User.builder()
                    .name("Test User")
                    .email("test@example.com")
                    .password(passwordEncoder.encode("test123"))
                    .role(User.Role.USER)
                    .status(User.UserStatus.ACTIVE)
                    .build();
            userRepository.save(testUser);
            log.info("Test kullanıcı oluşturuldu: test@example.com / test123");
        }
    }
}

