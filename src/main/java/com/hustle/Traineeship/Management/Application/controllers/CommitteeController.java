package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.service.CommitteeService;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private ProfessorService professorService;

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

    @GetMapping("/applicant/{universityId}/traineeships/skills")
    public String viewTraineeshipsBySkills(@PathVariable String universityId, Model model) {
        Student student = committeeService.getApplicantByUniversityId(universityId);
        if (student == null) {
            return "redirect:/committee/applicants?error=StudentNotFound";
        }
        List<TraineeshipPosition> positions = committeeService.searchPositionsForStudent(student.getId(), "skills");
        model.addAttribute("student", student);
        model.addAttribute("searchType", "Skills");
        model.addAttribute("traineeshipPositions", positions);
        return "applicant-details";
    }

    @GetMapping("/applicant/{universityId}/traineeships/all")
    public String viewTraineeshipsByAll(@PathVariable String universityId, Model model) {
        Student student = committeeService.getApplicantByUniversityId(universityId);
        if (student == null) {
            return "redirect:/committee/applicants?error=StudentNotFound";
        }
        List<TraineeshipPosition> positions = committeeService.searchPositionsForStudent(student.getId(), "all");
        model.addAttribute("student", student);
        model.addAttribute("searchType", "All Criteria");
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
            return "redirect:/committee/applicants";
        } else {
            redirectAttributes.addFlashAttribute("successMessage", message);
        }

        if (student != null && student.getUniversityId() != null) {
            return "redirect:/committee/applicant/" + student.getUniversityId() + "/details";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Assignment successful, but could not redirect to student details.");
            return "redirect:/committee/applicants";
        }
    }

    @GetMapping("/in-progress")
    @ResponseBody
    public List<TraineeshipPosition> listInProgressTraineeships() {
        return committeeService.getInProgressTraineeships();
    }

    @PostMapping("/finalize")
    public String finalizeTraineeship(@RequestParam Long positionId,
                                      @RequestParam boolean pass,
                                      RedirectAttributes redirectAttributes) {
        try {
            String message = committeeService.finalizeTraineeship(positionId, pass);
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/committee/fully-assigned";
    }

    @GetMapping("/traineeships/{positionId}/evaluation")
    @ResponseBody
    public ResponseEntity<?> getTraineeshipEvaluation(@PathVariable Long positionId) {
        try {
            Evaluation evaluation = committeeService.getEvaluationForTraineeship(positionId);
            if (evaluation != null) {
                return ResponseEntity.ok(evaluation);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evaluation not found for traineeship ID: " + positionId);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Committee committee = committeeService.findByUsername(principal.getName());
        model.addAttribute("committee", committee);
        List<TraineeshipPosition> fullyAssignedTraineeships = committeeService.getFullyAssignedTraineeships();
        model.addAttribute("fullyAssignedTraineeships", fullyAssignedTraineeships);
        return "committee-profile";
    }

    @GetMapping("/fully-assigned")
    public String viewFullyAssignedTraineeships(Model model) {
        List<TraineeshipPosition> fullyAssignedTraineeships = committeeService.getFullyAssignedTraineeships();
        model.addAttribute("fullyAssignedTraineeships", fullyAssignedTraineeships);
        return "committee-fully-assigned-list";
    }

    @GetMapping("/assign-professors")
    public String showAssignProfessorsPage(Model model) {
        List<TraineeshipPosition> traineeships = committeeService.getInProgressTraineeships();
        List<com.hustle.Traineeship.Management.Application.model.Professor> professors = professorService.getAllProfessors();
        model.addAttribute("traineeships", traineeships);
        model.addAttribute("professors", professors);
        return "assign-professors";
    }

    @PostMapping("/do-assign-supervisor")
    public String doAssignSupervisor(@RequestParam Long traineeshipId,
                                     @RequestParam Long professorId,
                                     RedirectAttributes redirectAttributes) {
        try {
            String message = committeeService.assignSupervisor(traineeshipId, professorId);
            redirectAttributes.addFlashAttribute("message", message);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Error: " + e.getMessage());
        }
        return "redirect:/committee/assign-professors";
    }
}
