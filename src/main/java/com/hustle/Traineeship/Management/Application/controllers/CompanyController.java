package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Display the company's profile creation form.
    // If the profile already exists, it loads it; otherwise, it provides a new instance.
    @GetMapping("/create_profile")
    public String showProfileCreationForm(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile-creation"; // This corresponds to company-profile-creation.html
    }

    // Process the company profile form submission to create (or save) the profile.
    @PostMapping("/create_profile")
    public String createProfile(@ModelAttribute("company") Company company, Principal principal) {
        // Ensure that the u sername matches the logged-in user
        companyService.updateCompanyProfile(company, principal.getName());
        return "redirect:/company/profile";
    }

    // Display the company's profile (after creation)
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile"; // This corresponds to company-profile.html
    }

    // Additional endpoints (e.g., for listing traineeship positions, evaluations, etc.)
    // can be added here as needed.
}
