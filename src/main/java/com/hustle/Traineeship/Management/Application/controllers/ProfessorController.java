package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    // Display the professor's profile creation form.
    // If the profile already exists, it loads it; otherwise, it provides a new instance.
    @GetMapping("/create_profile")
    public String showProfileCreationForm(Model model, Principal principal) {
        Professor   professor= professorService.findByUsername(principal.getName());
        model.addAttribute("professor", professor);
        return "professor-profile-creation";  // Corresponds to professor-profile-creation.html
    }

    // Process the professor profile form submission to create (or save) the profile.
    @PostMapping("/create_profile")
    public String createProfile(@ModelAttribute("professor") Professor professor, Principal principal) {
        // Ensure that the username matches the logged-in user
       professorService.updateProfessorProfile(professor, principal.getName());

        return "redirect:/professor/profile";
    }

    // Display the professor's profile (after creation)
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Professor professor = professorService.findByUsername(principal.getName());
        model.addAttribute("professor", professor);
        return "professor-profile"; // Corresponds to professor-profile.html
    }

    // Additional endpoints (e.g., for updating supervised positions or other professor-specific actions)
    // can be added here as needed.
}
