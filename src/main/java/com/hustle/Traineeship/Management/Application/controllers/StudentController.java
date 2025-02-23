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

    /**
     * Displays the student's profile page (studentProfile.html).
     * The 'student' attribute in the model is used by Thymeleaf to populate the form.
     */
    @GetMapping("/profile")
    public String getStudentProfile(Model model, Principal principal) {
        // 'principal.getName()' typically returns the logged-in username
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "studentProfile"; // Thymeleaf template name (studentProfile.html)
    }

    /**
     * Handles form submission from the student's profile page.
     * Updates the student's info and redirects back to /student/profile.
     */
    @PostMapping("/profile")
    public String updateStudentProfile(@ModelAttribute("student") Student student,
                                       Principal principal) {
        // Update the existing student's data in the DB
        studentsService.updateStudentProfile(student, principal.getName());
        return "redirect:/student/profile";
    }

    /**
     * Applies for a traineeship.
     * This could be triggered by a form or link on the student's profile page.
     */
    @PostMapping("/apply")
    public String applyForTraineeship(@RequestParam Long positionId, Principal principal) {
        // Retrieve the currently logged-in student
        Student student = studentsService.findByUsername(principal.getName());
        // Apply for traineeship
        studentsService.applyForTraineeship(student.getId(), positionId);
        // Redirect to some page (here, back to profile)
        return "redirect:/student/profile";
    }

    /**
     * Updates the student's logbook entries.
     * This could be triggered by a form on the student's profile or a dedicated logbook page.
     */
    @PostMapping("/logbook")
    public String updateLogbook(@RequestParam String entries, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        studentsService.updateLogbook(student.getId(), entries);
        return "redirect:/student/profile";
    }
}
