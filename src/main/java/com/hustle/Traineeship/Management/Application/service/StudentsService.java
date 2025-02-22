package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;
import org.springframework.stereotype.Service;


public interface StudentsService {
    Student createStudentProfile(Student student);
    String applyForTraineeship(Long studentId, Long positionId);
    String updateLogbook(Long studentId, String entries);
}
