package com.isro.pdfportal.service;

import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean staffNoExists(String staffNo) {
        return userRepository.existsByStaffNo(staffNo);
    }

    public User registerUser(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("USER");
        user.setActive(true);
        return userRepository.save(user);
    }
}