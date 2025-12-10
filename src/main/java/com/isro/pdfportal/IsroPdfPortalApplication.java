package com.isro.pdfportal;

import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class IsroPdfPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsroPdfPortalApplication.class, args);
    }

    // Create default admin if not present
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@pdfapp.local";
            String adminStaffNo = "ADM001";

            boolean adminExists = userRepository.existsByEmail(adminEmail)
                    || userRepository.existsByStaffNo(adminStaffNo);

            if (!adminExists) {
                User admin = new User();
                admin.setStaffNo(adminStaffNo);
                admin.setFullName("Application Administrator");
                admin.setEmail(adminEmail);
                admin.setDepartment("Application Management");
                admin.setDesignation("Admin");
                admin.setPhone(null);
                admin.setRole("ADMIN");
                admin.setActive(true);
                admin.setPassword(passwordEncoder.encode("Admin@123"));

                userRepository.save(admin);
            }
        };
    }
}
