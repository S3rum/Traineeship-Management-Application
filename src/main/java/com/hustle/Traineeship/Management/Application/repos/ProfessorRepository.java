package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsername(String username);
    @Query("""
        SELECT CASE WHEN (p.fullName IS NOT NULL)
        THEN TRUE ELSE FALSE END
        FROM Professor p
        WHERE p.id = :professorId
    """)
    boolean existsByUserId(@Param("professorId") Long professorId);
}