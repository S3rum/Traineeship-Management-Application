package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByCompanyIdAndStudentIsNull(Long companyId);
    List<TraineeshipPosition> findBySupervisorId(Long professorId);
    @Query("SELECT tp FROM TraineeshipPosition tp WHERE tp.student IS NULL")
    List<TraineeshipPosition> getAvailableTraineeshipPositions();
    List<TraineeshipPosition> findByCompanyId(Long companyId);
}