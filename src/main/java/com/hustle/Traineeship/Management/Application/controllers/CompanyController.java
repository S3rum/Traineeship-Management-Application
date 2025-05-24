package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @GetMapping("/create_profile")
    public String showProfileForm(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile-creation";
    }

    @PostMapping("/create_profile")
    public String createOrUpdateProfile(@ModelAttribute("company") Company company, Principal principal) {
        companyService.updateCompanyProfile(company, principal.getName());
        return "redirect:/company/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company-profile";
    }

    @GetMapping("/announce_traineeship")
    public String showAnnounceForm(Model model) {
        model.addAttribute("traineeshipPosition", new TraineeshipPosition());
        return "company-announce-traineeship";
    }

    @PostMapping("/announce_traineeship")
    public String announceTraineeship(@ModelAttribute("traineeshipPosition") TraineeshipPosition position,
                                      Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        position.setCompany(company);
        companyService.announcePosition(position);
        return "redirect:/company/profile";
    }

    @GetMapping("/traineeships")
    public String viewTraineeships(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        List<TraineeshipPosition> positions = companyService.getAvailablePositions(company.getId());
        model.addAttribute("traineeships", positions);
        return "company-traineeships";
    }

    @PostMapping("/traineeships/{positionId}/delete")
    public String deleteTraineeshipPosition(@PathVariable Long positionId, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        TraineeshipPosition position = companyService.getTraineeshipPositionById(positionId);

        if (position == null || position.getCompany() == null || !position.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this traineeship position.");
        }

        companyService.deletePosition(positionId);
        return "redirect:/company/traineeships";
    }

    @GetMapping("/traineeships/{positionId}/evaluate")
    public String showEvaluationForm(@PathVariable Long positionId, Principal principal, Model model) {
        Company company = companyService.findByUsername(principal.getName());
        TraineeshipPosition position = companyService.getTraineeshipPositionById(positionId);
        if (position == null || position.getCompany() == null || !position.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("Unauthorized to evaluate this traineeship");
        }
        Evaluation evaluation = position.getEvaluation();
        if (evaluation == null) {
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position);
        }
        model.addAttribute("evaluation", evaluation);
        return "company-evaluation-form";
    }

    @PostMapping("/traineeships/{positionId}/evaluate")
    public String submitEvaluation(@PathVariable Long positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluationForm,
                                   Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        TraineeshipPosition position = companyService.getTraineeshipPositionById(positionId);
        if (position == null || position.getCompany() == null || !position.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("Unauthorized to evaluate this traineeship");
        }
        evaluationForm.setTraineeshipPosition(position);
        companyService.evaluateTraineeship(evaluationForm);
        return "redirect:/company/traineeships";
    }
}
