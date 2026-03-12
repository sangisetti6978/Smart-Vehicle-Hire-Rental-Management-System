package com.vehiclerental.config;

import com.vehiclerental.model.User;
import com.vehiclerental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Seed default admin if not present
        if (!userRepository.existsByEmail("admin@vehiclerental.com")) {
            User admin = new User();
            admin.setEmail("admin@vehiclerental.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Admin");
            admin.setPhone("0000000000");
            admin.setRole(User.UserRole.ADMIN);
            admin.setIsVerified(true);
            admin.setIsActive(true);
            userRepository.save(admin);
            System.out.println("✅ Default admin user created: admin@vehiclerental.com / admin123");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }
}
