package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    // Display the student's profile page using Thymeleaf.
    @GetMapping("/create_profile")
    public String showProfileForm(Model model, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile-creation";  // Corresponding template: student-profile-creation.html
    }

    // Process profile updates
    @PostMapping("/create_profile")
    public String updateProfile(@ModelAttribute("student") Student student, Principal principal) {
        studentsService.updateStudentProfile(student, principal.getName());
        return "redirect:/student/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile"; // Corresponding template: student-profile.html
    }

    // GET endpoint for displaying the internship selection page.
    @GetMapping("/{studentId}/apply")
    public String showTraineeshipSelection(@PathVariable Long studentId, Model model) {
        // Retrieve the student based on the provided studentId.
        Student student = studentsService.findStudentById(studentId);
        model.addAttribute("student", student);

        // Retrieve the available traineeship positions for the student.
        List<TraineeshipPosition> availablePositions = studentsService.getAvailableTraineeshipPositions();
        model.addAttribute("positions", availablePositions);

        // Return the Thymeleaf template for selecting a traineeship.
        return "traineeship-selection"; // Create traineeship-selection.html accordingly.
    }

    // POST endpoint for applying for a traineeship.
    @PostMapping("/{studentId}/apply")
    public String applyForTraineeship(@PathVariable Long studentId, @RequestParam Long positionId) {
        return studentsService.applyForTraineeship(studentId, positionId);
    }

    // Endpoint for updating the student's logbook.
    @PostMapping("/{studentId}/logbook")
    public String updateLogbook(@PathVariable Long studentId, @RequestBody String entries) {
        return studentsService.updateLogbook(studentId, entries);
    }
}
