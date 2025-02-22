package com.hustle.Traineeship.Management.Application.repos;
import com.hustle.Traineeship.Management.Application.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}