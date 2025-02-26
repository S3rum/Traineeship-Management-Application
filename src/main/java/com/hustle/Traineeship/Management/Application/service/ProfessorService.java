package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;

public interface ProfessorService {
    Professor createProfessorProfile(Professor professor);
    List<TraineeshipPosition> getSupervisedPositions(Long professorId);
    String evaluateTraineeship(Long positionId, int motivationRating, int effectivenessRating, int efficiencyRating, int companyFacilitiesRating, int companyGuidanceRating);

    boolean professorProfileExists(Long userId);

    Professor findByUsername(String username);
    Professor updateProfessorProfile(Professor updatedProfessor, String username);
}
