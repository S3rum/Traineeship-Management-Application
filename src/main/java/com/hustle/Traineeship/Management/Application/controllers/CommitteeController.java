package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.service.CommitteeService;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/committee")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private StudentsService studentsService;

    @GetMapping("/applicants")
    public String viewApplicants(Model model) {
        List<Student> applicants = committeeService.getApplicants();
        model.addAttribute("applicants", applicants);
        return "committee-applicants";
    }

    @GetMapping("/applicant/{universityId}/details")
    public String viewApplicantDetails(@PathVariable String universityId, Model model) {
        Student student = committeeService.getApplicantByUniversityId(universityId);
        if (student == null) {
            return "redirect:/committee/applicants?error=StudentNotFound";
        }
        model.addAttribute("student", student);
        return "applicant-details";
    }

    @GetMapping("/applicant/{universityId}/traineeships/location")
    public String viewTraineeshipsByLocation(@PathVariable String universityId, Model model) {
        Student student = committeeService.getApplicantByUniversityId(universityId);
        if (student == null) {
            return "redirect:/committee/applicants?error=StudentNotFound";
        }
        List<TraineeshipPosition> positions = committeeService.searchPositionsForStudent(student.getId(), "location");
        model.addAttribute("student", student);
        model.addAttribute("searchType", "Location");
        model.addAttribute("traineeshipPositions", positions);
        return "applicant-details";
    }

    @GetMapping("/applicant/{universityId}/traineeships/interests")
    public String viewTraineeshipsByInterests(@PathVariable String universityId, Model model) {
        Student student = committeeService.getApplicantByUniversityId(universityId);
        if (student == null) {
            return "redirect:/committee/applicants?error=StudentNotFound";
        }
        List<TraineeshipPosition> positions = committeeService.searchPositionsForStudent(student.getId(), "interests");
        model.addAttribute("student", student);
        model.addAttribute("searchType", "Interests");
        model.addAttribute("traineeshipPositions", positions);
        return "applicant-details";
    }

    @GetMapping("/applicant/{universityId}/traineeships/both")
    public String viewTraineeshipsByBoth(@PathVariable String universityId, Model model) {
        Student student = committeeService.getApplicantByUniversityId(universityId);
        if (student == null) {
            return "redirect:/committee/applicants?error=StudentNotFound";
        }
        List<TraineeshipPosition> positions = committeeService.searchPositionsForStudent(student.getId(), "both");
        model.addAttribute("student", student);
        model.addAttribute("searchType", "Interests and Location");
        model.addAttribute("traineeshipPositions", positions);
        return "applicant-details";
    }

    @GetMapping("/positions")
    @ResponseBody
    public List<TraineeshipPosition> searchPositions(@RequestParam Long studentId,
                                                     @RequestParam String strategy) {
        return committeeService.searchPositionsForStudent(studentId, strategy);
    }

    @PostMapping("/assign")
    public String assignTraineeship(@RequestParam Long studentId,
                                    @RequestParam Long positionId,
                                    RedirectAttributes redirectAttributes) {
        String message = committeeService.assignTraineeship(studentId, positionId);

        Student student = null;
        try {
            student = studentsService.findStudentById(studentId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Student not found for ID " + studentId + ".");
            return "redirect:/committee/applicants";
        }

        if (message.startsWith("Error:")) {
            redirectAttributes.addFlashAttribute("errorMessage", message);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", message);
        }

        if (student != null && student.getUniversityId() != null) {
            return "redirect:/committee/applicant/" + student.getUniversityId() + "/details";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", (message.startsWith("Error:") ? message : "Assignment successful, but could not redirect to student details."));
            return "redirect:/committee/applicants";
        }
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
        Committee committee = committeeService.findByUsername(principal.getName());
        model.addAttribute("committee", committee);
        return "committee-profile";
    }
}
