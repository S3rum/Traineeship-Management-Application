package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;



@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Display the company's profile creation form.
    @GetMapping("/create_profile")
    public String showProfileForm(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile-creation";
    }

    // Process the company profile form submission.
    @PostMapping("/create_profile")
    public String createOrUpdateProfile(@ModelAttribute("company") Company company, Principal principal) {
        companyService.updateCompanyProfile(company, principal.getName());
        return "redirect:/company/profile";
    }

    // Display the company's profile.
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile";
    }

    // GET endpoint to display the announcement form.
    @GetMapping("/announce_traineeship")
    public String showAnnounceForm(Model model) {
        model.addAttribute("traineeshipPosition", new TraineeshipPosition());
        return "company-announce-traineeship";
    }

    // POST endpoint to process the announcement form.
    @PostMapping("/announce_traineeship")
    public String announceTraineeship(@ModelAttribute("traineeshipPosition") TraineeshipPosition position,
                                      Principal principal) {
        // Retrieve the company from the service.
        Company company = companyService.findByUsername(principal.getName());
        position.setCompany(company);
        // Use the service method to announce the position.
        companyService.announcePosition(position);
        return "redirect:/company/profile"; // Adjust redirect as needed.
    }

    // GET endpoint to view the traineeship positions created by the company.
    @GetMapping("/traineeships")
    public String viewTraineeships(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        List<TraineeshipPosition> positions = companyService.getAvailablePositions(company.getId());
        model.addAttribute("traineeships", positions);
        return "company-traineeships";
    }

    // POST endpoint to delete a traineeship position.
    @PostMapping("/traineeships/{positionId}/delete")
    public String deleteTraineeshipPosition(@PathVariable Long positionId, Principal principal) {
        Company  company = companyService.findByUsername(principal.getName());
        // Optionally, you can perform an authorization check inside the service as well.
        // Here, we assume that the service's deletePosition method performs the check.
        companyService.deletePosition(positionId);
        return "redirect:/company/traineeships";
    }

    // Show a form to fill in the evaluation for a traineeship position.
    @GetMapping("/traineeships/{positionId}/evaluate")
    public String showEvaluationForm(@PathVariable Long positionId, Principal principal, Model model) {
        Company company = companyService.findByUsername(principal.getName());
        TraineeshipPosition position = companyService.getTraineeshipPositionById(positionId);
        // Check that the position belongs to the logged-in company.
        if (!position.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Unauthorized to evaluate this traineeship");
        }
        Evaluation evaluation = position.getEvaluation();
        if (evaluation == null) {
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position);
        }
        model.addAttribute("evaluation", evaluation);
        return "company-evaluation-form";
    }

    // Process the submitted evaluation form.
    @PostMapping("/traineeships/{positionId}/evaluate")
    public String submitEvaluation(@PathVariable Long positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluationForm,
                                   Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        TraineeshipPosition position = companyService.getTraineeshipPositionById(positionId);
        if (!position.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Unauthorized to evaluate this traineeship");
        }
        evaluationForm.setTraineeshipPosition(position);
        companyService.evaluateTraineeship(evaluationForm);
        return "redirect:/company/traineeships";
    }
}
