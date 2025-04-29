package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.service.CommitteeService;
import com.hustle.Traineeship.Management.Application.repos.CommitteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/committee")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private CommitteeRepository committeeRepository;

    @GetMapping("/applicants")
    public String viewApplicants(Model model) {
        List<Student> applicants = committeeService.getApplicants();
        model.addAttribute("applicants", applicants);
        return "committee-applicants";
    }

    @GetMapping("/positions")
    public List<TraineeshipPosition> searchPositions(@RequestParam Long studentId,
                                                     @RequestParam String strategy) {
        return committeeService.searchPositionsForStudent(studentId, strategy);
    }

    @PostMapping("/assign")
    public String assignTraineeship(@RequestParam Long studentId,
                                    @RequestParam Long positionId) {
        return committeeService.assignTraineeship(studentId, positionId);
    }

    @PostMapping("/assign-supervisor")
    public String assignSupervisor(@RequestParam Long positionId,
                                   @RequestParam String strategy) {
        return committeeService.assignSupervisor(positionId, strategy);
    }

    @GetMapping("/in-progress")
    public List<?> listInProgressTraineeships() {
        return committeeService.getInProgressTraineeships();
    }

    @PostMapping("/finalize")
    public String finalizeTraineeship(@RequestParam Long positionId,
                                      @RequestParam boolean pass) {
        return committeeService.finalizeTraineeship(positionId, pass);
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Committee committee = committeeRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Committee not found for username: " + principal.getName()));
        model.addAttribute("committee", committee);
        return "committee-profile";
    }
}
