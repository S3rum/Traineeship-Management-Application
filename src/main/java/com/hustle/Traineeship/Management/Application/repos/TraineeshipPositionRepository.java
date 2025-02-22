package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByCompanyIdAndStudentIsNull(Long companyId);
    List<TraineeshipPosition> findBySupervisorId(Long professorId);
}