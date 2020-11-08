package com.gsg.youtubemonitor;

import com.gsg.youtubemonitor.model.User;
import com.gsg.youtubemonitor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class YoutubeMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeMonitorApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return (args) -> {
            if (userRepository.findByUsername("admin").isPresent()) {
                return;
            }

            User user = User.builder()
                            .username("admin")
                            .passwordHash("$2y$10$sh0U3ORaOSYBKNxRtu6R5eqHMnJKnmsadbL9m/6q2Z7PN64iQjIBy")
                            .countryCode("GE")
                            .jobRunMinute(0)
                            .build();
            userRepository.save(user);
        };
    }
}
