package com.example.restapi;

import com.example.restapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataInitializerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void dataInitializer_seedsThreeUsers() {
        // DataInitializer runs on startup; verify at least the three seeded users exist
        assertThat(userRepository.count()).isGreaterThanOrEqualTo(3);
        assertThat(userRepository.findAll())
                .extracting("email")
                .contains(
                        "alice.smith@example.com",
                        "bob.jones@example.com",
                        "carol.white@example.com"
                );
    }
}
