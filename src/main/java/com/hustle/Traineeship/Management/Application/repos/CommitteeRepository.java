package com.hustle.Traineeship.Management.Application.repos;
import com.hustle.Traineeship.Management.Application.model.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommitteeRepository extends JpaRepository<Committee, Long>{
    Optional<Committee> findByUsername(String username);
}
