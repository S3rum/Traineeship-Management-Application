package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    @Query("""
        SELECT CASE WHEN (
            s.fullName IS NOT NULL
        )
        THEN TRUE ELSE FALSE END
        FROM Student s
        WHERE s.id = :studentId
    """)
    boolean existsByUserId(@Param("studentId") Long studentId);

}
