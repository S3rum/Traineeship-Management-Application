package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;


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

    @GetMapping("/supervised_positions")
    public String showSupervisedPositions(Principal principal, Model model) {
        // 1. Get the current professor from the DB by principal username.
        Professor professor = professorService.findByUsername(principal.getName());

        // 2. Fetch the list of positions supervised by this professor.
        List<TraineeshipPosition> supervisedPositions =
                professorService.getSupervisedPositions(professor.getId());

        // 3. Add the positions to the model.
        model.addAttribute("positions", supervisedPositions);

        // 4. Return the Thymeleaf view that displays them.
        return "professor-supervised-positions";
    }
    @GetMapping("/supervised_positions/evaluate/{positionId}")
    public String showEvaluationForm(@PathVariable Long positionId, Model model) {

        // Retrieve the traineeship position
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));


        // If an Evaluation object already exists, load it; otherwise create a new one
        Evaluation evaluation = position.getEvaluation();
        if (evaluation == null) {
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position);
        }

        // Add to the model
        model.addAttribute("evaluation", evaluation);
        return "professor-evaluation-form"; // A Thymeleaf template
    }
    @PostMapping("/traineeships/{positionId}/evaluate")
    public String submitEvaluation(@PathVariable Long positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluation) {
        // You may perform an authorization check here to ensure the professor is allowed to evaluate.

        // Ensure that the evaluation is associated with the correct position
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));
        evaluation.setTraineeshipPosition(position);

        // Call the new evaluation method in the service layer
        professorService.evaluateTraineeship(evaluation);

        // Redirect to the supervised positions page after evaluation is submitted
        return "redirect:/professor/supervised_positions";
    }


}
