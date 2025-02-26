package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.config.CustomAuthenticationSuccessHandler;
import com.hustle.Traineeship.Management.Application.model.User;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import com.hustle.Traineeship.Management.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentsService studentsService;

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
            userService.registerUser(user);
            return "redirect:/auth/login";


        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }


    public AuthenticationSuccessHandler customSuccessHandler(StudentsService studentsService, ProfessorService professorService) {
        return new CustomAuthenticationSuccessHandler(studentsService, professorService);
    }

    // Display the login form using Thymeleaf
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Thymeleaf will resolve this to login.html
    }

}
