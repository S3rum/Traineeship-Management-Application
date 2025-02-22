package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/committee")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @GetMapping("/applicants")
    public List<?> listApplicants() {
        return committeeService.getApplicants();
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
}
