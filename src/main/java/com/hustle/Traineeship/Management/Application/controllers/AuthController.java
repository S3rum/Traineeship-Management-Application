package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.User;
import com.hustle.Traineeship.Management.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Display the registration form using Thymeleaf
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";  // resolves to register.html
    }

    // Process the registration form submission
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        try {
            // In the registerUser method, ensure that the password is encoded (e.g., using BCryptPasswordEncoder)
            userService.registerUser(user);
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            // Return back to the registration form with an error
            return "register";
        }
    }

    // Display the login form using Thymeleaf
    @GetMapping("/login")
    public String login() {
        return "login"; // Thymeleaf will resolve this to login.html
    }
}
