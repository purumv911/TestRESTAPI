package com.example.restapi;

import com.example.restapi.model.User;
import com.example.restapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        userRepository.save(new User("Alice", "Smith", "alice.smith@example.com", "555-0101"));
        userRepository.save(new User("Bob", "Jones", "bob.jones@example.com", "555-0102"));
        userRepository.save(new User("Carol", "White", "carol.white@example.com", "555-0103"));
    }
}
