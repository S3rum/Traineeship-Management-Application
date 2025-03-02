package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsername(String username);
}