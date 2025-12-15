package com.isro.pdfportal.controller;

import com.isro.pdfportal.config.CustomUserDetails;
import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // List all users
    @GetMapping
    public String listUsers(Authentication authentication, Model model) {
        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        model.addAttribute("users", userRepository.findAllByOrderByFullNameAsc());
        return "admin-users";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id,
                           Authentication authentication,
                           Model model) {
        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return "redirect:/login";
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "admin-user-edit";
    }

    // Handle save
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User formUser) {

        User existing = userRepository.findById(formUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setFullName(formUser.getFullName());
        existing.setStaffNo(formUser.getStaffNo());
        existing.setDepartment(formUser.getDepartment());
        existing.setDesignation(formUser.getDesignation());
        existing.setEmail(formUser.getEmail());
        existing.setPhone(formUser.getPhone());
        existing.setRole(formUser.getRole());
        existing.setActive(formUser.isActive());

        userRepository.save(existing);

        return "redirect:/admin/users";
    }
}