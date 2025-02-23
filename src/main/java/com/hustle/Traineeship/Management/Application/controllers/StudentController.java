package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    @PostMapping("/profile")
    public Student createProfile(@RequestBody Student student) {
        return studentsService.createStudentProfile(student);
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model) {
        // Create a new Student object (or load the logged-in user's profile)
        model.addAttribute("student", new Student());
        return "student-profile"; // This should match your Thymeleaf template name (student-profile.html)
    }

    @PostMapping("/{studentId}/apply")
    public String applyForTraineeship(@PathVariable Long studentId, @RequestParam Long positionId) {
        return studentsService.applyForTraineeship(studentId, positionId);
    }

    @PostMapping("/{studentId}/logbook")
    public String updateLogbook(@PathVariable Long studentId, @RequestBody String entries) {
        return studentsService.updateLogbook(studentId, entries);
    }
}
