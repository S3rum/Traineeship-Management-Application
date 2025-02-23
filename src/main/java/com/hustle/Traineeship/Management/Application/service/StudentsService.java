package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;
import org.springframework.stereotype.Service;

@Service
public interface StudentsService {

    // Create a new student profile during registration
    Student createStudentProfile(Student student);

    // Retrieve the student profile based on the username
    Student findByUsername(String username);

    // Update the student profile; the 'username' parameter is used to ensure the correct student is updated
    Student updateStudentProfile(Student student, String username);

    // Process a student's application for a traineeship position
    String applyForTraineeship(Long studentId, Long positionId);

    // Update the logbook entries for the student
    String updateLogbook(Long studentId, String entries);
}
