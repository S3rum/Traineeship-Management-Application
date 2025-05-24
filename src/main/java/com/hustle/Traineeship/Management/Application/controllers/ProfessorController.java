package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;


    @GetMapping("/create_profile")
    public String showProfileCreationForm(Model model, Principal principal) {
        Professor   professor= professorService.findByUsername(principal.getName());
        model.addAttribute("professor", professor);
        return "professor-profile-creation";
    }

    @PostMapping("/create_profile")
    public String createProfile(@ModelAttribute("professor") Professor professor, Principal principal) {
        professorService.updateProfessorProfile(professor, principal.getName());

        return "redirect:/professor/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Professor professor = professorService.findByUsername(principal.getName());
        model.addAttribute("professor", professor);
        return "professor-profile";
    }

    @GetMapping("/supervised_positions")
    public String showSupervisedPositions(Principal principal, Model model) {
        Professor professor = professorService.findByUsername(principal.getName());

        List<TraineeshipPosition> supervisedPositions =
                professorService.getSupervisedPositions(professor.getId());
        model.addAttribute("positions", supervisedPositions);

        return "professor-supervised-positions";
    }
    @GetMapping("/supervised_positions/evaluate/{positionId}")
    public String showEvaluationForm(@PathVariable Long positionId, Model model, Principal principal) {
        Professor authenticatedProfessor = professorService.findByUsername(principal.getName());
        TraineeshipPosition position = professorService.findTraineeshipPositionById(positionId);

        if (position == null || position.getSupervisor() == null || !position.getSupervisor().getId().equals(authenticatedProfessor.getId())) {
            throw new AccessDeniedException("You are not authorized to evaluate this traineeship position.");
        }

        Evaluation evaluation = position.getEvaluation();
        if (evaluation == null) {
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position);
        }

        model.addAttribute("evaluation", evaluation);
        return "professor-evaluation-form";
    }
    @PostMapping("/traineeships/{positionId}/evaluate")
    public String submitEvaluation(@PathVariable Long positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluation,
                                   Principal principal) {
        Professor authenticatedProfessor = professorService.findByUsername(principal.getName());
        TraineeshipPosition position = professorService.findTraineeshipPositionById(positionId);

        if (position == null || position.getSupervisor() == null || !position.getSupervisor().getId().equals(authenticatedProfessor.getId())) {
            throw new AccessDeniedException("You are not authorized to submit an evaluation for this traineeship position.");
        }

        professorService.evaluateTraineeship(evaluation);

        return "redirect:/professor/supervised_positions";
    }


}
