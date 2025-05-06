package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.TraineeshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {

    List<TraineeshipPosition> findByCompanyIdAndStudentIsNull(Long companyId);


    @Query("SELECT tp FROM TraineeshipPosition tp WHERE tp.student IS NULL")
    List<TraineeshipPosition> getAvailableTraineeshipPositions();

    List<TraineeshipPosition> findByCompanyId(Long companyId);

    List<TraineeshipPosition> findBySupervisorId(Long professorId);

    List<TraineeshipPosition> findByStudentIsNotNullAndEndDateAfter(java.time.LocalDate currentDate);

    List<TraineeshipPosition> findByStudentIsNotNullAndSupervisorIsNotNullAndEndDateAfter(java.time.LocalDate currentDate);

    List<TraineeshipPosition> findByStatus(TraineeshipStatus status);

    // Remove or update this redundant method:
    // List<TraineeshipPosition> findByProfessorId(Long supervisor);
}
