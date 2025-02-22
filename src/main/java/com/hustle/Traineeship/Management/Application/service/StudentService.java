package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;

public interface StudentService {
    Student createStudentProfile(Student student);
    String applyForTraineeship(Long studentId, Long positionId);
    String updateLogbook(Long studentId, String entries);
}
