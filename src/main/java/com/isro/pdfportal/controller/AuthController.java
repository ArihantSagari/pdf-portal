package com.isro.pdfportal.controller;

import com.isro.pdfportal.entity.User;
import com.isro.pdfportal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid email/staff number or password.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("user") User formUser,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Password and Confirm Password do not match.");
            return "register";
        }

        if (userService.emailExists(formUser.getEmail())) {
            model.addAttribute("errorMessage", "Email already registered.");
            return "register";
        }

        if (userService.staffNoExists(formUser.getStaffNo())) {
            model.addAttribute("errorMessage", "Staff number already registered.");
            return "register";
        }

        userService.registerUser(formUser, password);
        model.addAttribute("successMessage", "Registration successful. You can now log in.");
        return "login";
    }
}