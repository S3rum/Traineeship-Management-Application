package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    @PostMapping("/profile")
    public Student createProfile(@RequestBody Student student) {
        return studentsService.createStudentProfile(student);
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
