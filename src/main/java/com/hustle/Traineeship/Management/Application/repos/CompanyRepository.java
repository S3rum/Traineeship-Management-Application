package com.hustle.Traineeship.Management.Application.repos;
import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUsername(String username);
    @Query("""
        SELECT CASE WHEN (c.companyName IS NOT NULL)
        THEN TRUE ELSE FALSE END
        FROM Company c
        WHERE c.id = :companyId
    """)
    boolean existsByUserId(@Param("companyId") Long companyId);
}