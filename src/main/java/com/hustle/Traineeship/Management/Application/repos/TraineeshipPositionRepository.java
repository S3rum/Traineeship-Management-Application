package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.TraineeshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {

    List<TraineeshipPosition> findByStudentIsNull();

    List<TraineeshipPosition> findByCompanyId(Long companyId);

    List<TraineeshipPosition> findBySupervisorId(Long professorId);

    List<TraineeshipPosition> findByStudentIsNotNullAndSupervisorIsNotNullAndEndDateAfter(java.time.LocalDate currentDate);

    List<TraineeshipPosition> findByStatus(TraineeshipStatus status);
}
