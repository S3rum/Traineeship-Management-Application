package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    // Display the student's profile page using Thymeleaf.
    // The Principal object is used to get the username of the logged-in user.
    @GetMapping("/create_profile")
    public String showProfileForm(Model model, Principal principal) {
        System.out.println();
        // Load the logged-in student's profile
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile-creation";  // This should correspond to student-profile-creation.html in your templates folder
    }

    // Optionally, process profile updates
    @PostMapping("/create_profile")
    public String updateProfile(@ModelAttribute("student") Student student, Principal principal) {
        studentsService.updateStudentProfile(student, principal.getName());
        return "redirect:/student/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        // Ensure that studentsService.findByUsername returns a valid Student object
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile"; // This should match your Thymeleaf template name
    }


    // Endpoint for applying for a traineeship.
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
