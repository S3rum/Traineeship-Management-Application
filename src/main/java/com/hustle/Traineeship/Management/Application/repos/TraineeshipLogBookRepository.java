package com.hustle.Traineeship.Management.Application.repos;

import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TraineeshipLogBookRepository extends JpaRepository<TraineeshipLogBook, Long> {
    // Find all log entries for a given student
    List<TraineeshipLogBook> findByStudentId(Long studentId);
}
