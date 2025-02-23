package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studentsService")
public class StudentsServiceImpl implements StudentsService {

    @Autowired
    private StudentRepository studentRepository;

    // Assume you have a TraineeshipPositionRepository injected as well for application logic.

    @Override
    public Student createStudentProfile(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));
    }

    @Override
    public Student updateStudentProfile(Student student, String username) {
        // Implement update logic here (e.g., load the existing student, update fields, and save)
        return studentRepository.save(student);
    }

    @Override
    public String applyForTraineeship(Long studentId, Long positionId) {
        // Logic to link the student with the chosen traineeship position.
        return "Application successful";
    }

    @Override
    public String updateLogbook(Long studentId, String entries) {
        // Logic to update or create the logbook entry for the student.
        return "Logbook updated";
    }
}
