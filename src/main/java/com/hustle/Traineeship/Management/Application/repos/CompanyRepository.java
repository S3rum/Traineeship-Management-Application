package com.hustle.Traineeship.Management.Application.repos;
import com.hustle.Traineeship.Management.Application.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUsername(String username);
}