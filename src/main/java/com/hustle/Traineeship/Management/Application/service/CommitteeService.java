package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import java.util.List;

public interface CommitteeService {
    List<Student> getApplicants();
    Student getApplicantByUniversityId(String universityId);
    List<TraineeshipPosition> searchPositionsForStudent(Long studentId, String strategy);
    String assignTraineeship(Long studentId, Long positionId);
    String assignSupervisor(Long positionId, Long professorId);
    List<TraineeshipPosition> getInProgressTraineeships();
    List<TraineeshipPosition> getFullyAssignedTraineeships();
    String finalizeTraineeship(Long positionId, boolean pass);
    Committee findByUsername(String username);
    Evaluation getEvaluationForTraineeship(Long positionId);
}