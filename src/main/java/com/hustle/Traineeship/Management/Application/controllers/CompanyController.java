package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.EvaluationRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
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

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    // Display the company's profile creation form.
    @GetMapping("/create_profile")
    public String showProfileCreationForm(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile-creation"; // This corresponds to company-profile-creation.html
    }

    // Process the company profile form submission.
    @PostMapping("/create_profile")
    public String createProfile(@ModelAttribute("company") Company company, Principal principal) {
        companyService.updateCompanyProfile(company, principal.getName());
        return "redirect:/company/profile";
    }

    // Display the company's profile.
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile"; // This corresponds to company-profile.html
    }

    // GET endpoint to display the announcement form.
    @GetMapping("/announce_traineeship")
    public String showAnnounceForm(Model model) {
        model.addAttribute("traineeshipPosition", new TraineeshipPosition());
        return "company-announce-traineeship"; // This corresponds to company-announce-traineeship.html
    }

    // POST endpoint to process the announcement form.
    @PostMapping("/announce_traineeship")
    public String announceTraineeship(@ModelAttribute("traineeshipPosition") TraineeshipPosition traineeshipPosition,
                                      Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        traineeshipPosition.setCompany(company);
        traineeshipPositionRepository.save(traineeshipPosition);
        return "redirect:/company/dashboard"; // Adjust redirect as needed.
    }

    // NEW: GET endpoint to view the traineeship positions created by the company.
    @GetMapping("/traineeships")
    public String viewTraineeships(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        List<TraineeshipPosition> positions = traineeshipPositionRepository.findByCompanyId(company.getId());
        model.addAttribute("traineeships", positions);
        return "company-traineeships"; // This corresponds to company-traineeships.html
    }
    // CompanyController.java

    @PostMapping("/traineeships/{positionId}/delete")
    public String deleteTraineeshipPosition(@PathVariable Long positionId, Principal principal) {
        // Retrieve the logged-in company
        Company company = companyService.findByUsername(principal.getName());

        // Retrieve the traineeship position
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        // Verify that the position belongs to the logged-in company
        if (!position.getCompany().getId().equals(company.getId())) {
            // You can handle this however you prefer (e.g., throw an exception or return an error page)
            throw new RuntimeException("Unauthorized to delete this traineeship");
        }

        // Delete the position
        traineeshipPositionRepository.delete(position);

        // Redirect back to the list of traineeships
        return "redirect:/company/traineeships";
    }

    // Show a form to fill in the evaluation
    @GetMapping("/traineeships/{positionId}/evaluate")
    public String showEvaluationForm(@PathVariable Long positionId, Principal principal, Model model) {
        // Retrieve the logged-in company
        Company company = companyService.findByUsername(principal.getName());

        // Retrieve the traineeship position
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        // Ensure the position belongs to the company
        if (!position.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Unauthorized to evaluate this traineeship");
        }

        // If an Evaluation object already exists, load it; otherwise create a new one
        Evaluation evaluation = position.getEvaluation();
        if (evaluation == null) {
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position);
        }

        // Add to the model
        model.addAttribute("evaluation", evaluation);
        return "company-evaluation-form"; // A Thymeleaf template
    }

    // Process the submitted evaluation form
    @PostMapping("/traineeships/{positionId}/evaluate")
    public String submitEvaluation(@PathVariable Long positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluationForm,
                                   Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        if (!position.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Unauthorized to evaluate this traineeship");
        }

        // Link the evaluation to the traineeship
        evaluationForm.setTraineeshipPosition(position);

        // If there's already an evaluation in the DB, preserve its ID
        if (position.getEvaluation() != null) {
            evaluationForm.setId(position.getEvaluation().getId());
        }

        // Save or update
        evaluationRepository.save(evaluationForm);

        // Redirect to the traineeships list or wherever you prefer
        return "redirect:/company/traineeships";
    }

}
