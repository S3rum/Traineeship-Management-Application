package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Role;
import com.hustle.Traineeship.Management.Application.model.Student;
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
            if(user.getRole() == Role.STUDENT) {
                // Create a Student from the generic user details
                Student student = new Student();
                student.setUsername(user.getUsername());
                student.setPassword(user.getPassword());
                student.setRole(user.getRole());
                // Populate additional student-specific fields as needed

                userService.registerUser(student);
            } else {
                userService.registerUser(user);
            }
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }


    // Display the login form using Thymeleaf
    @GetMapping("/login")
    public String login() {
        return "login"; // Thymeleaf will resolve this to login.html
    }
}
