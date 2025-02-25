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
    public Student updateStudentProfile(Student updatedStudent, String username) {
        // Load the existing student by username
        Student existingStudent = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));

        // Update the necessary fields
        existingStudent.setFullName(updatedStudent.getFullName());
        existingStudent.setUniversityId(updatedStudent.getUniversityId());
        existingStudent.setInterests(updatedStudent.getInterests());
        existingStudent.setSkills(updatedStudent.getSkills());
        existingStudent.setPreferredLocation(updatedStudent.getPreferredLocation());

        // Save the updated student to the database
        return studentRepository.save(existingStudent);
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

    @Override
    public boolean studentProfileExists(Long userId) {
        return studentRepository.existsByUserId(userId);
    }
}
