package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.Committee;
import java.util.List;

public interface CommitteeService {
    List<Student> getApplicants();
    List<TraineeshipPosition> searchPositionsForStudent(Long studentId, String strategy);
    String assignTraineeship(Long studentId, Long positionId);
    String assignSupervisor(Long positionId, String strategy);
    List<?> getInProgressTraineeships();
    String finalizeTraineeship(Long positionId, boolean pass);
    Committee findByUsername(String username);
}