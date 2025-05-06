package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;

public interface ProfessorService {
    Professor createProfessorProfile(Professor professor);
    List<TraineeshipPosition> getSupervisedPositions(Long professorId);
    void evaluateTraineeship(Evaluation evaluation);


    Professor findByUsername(String username);
    void updateProfessorProfile(Professor updatedProfessor, String username);
    TraineeshipPosition findTraineeshipPositionById(Long positionId);
}
