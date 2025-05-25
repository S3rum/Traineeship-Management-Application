package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    List<Student> findByTraineeshipIdIs(Long traineeshipId);
    Optional<Student> findByUniversityId(String universityId);
}
