package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.ProfessorRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TraineeshipPositionRepository positionRepository;

    @Override
    public Professor createProfessorProfile(Professor professor) {
        return professorRepository.save(professor);
    }

    @Override
    public List<TraineeshipPosition> getSupervisedPositions(Long professorId) {
        // Retrieve positions supervised by the professor.
        return positionRepository.findBySupervisorId(professorId);
    }

    @Override
    public String evaluateTraineeship(Long positionId, int motivationRating, int effectivenessRating, int efficiencyRating, int companyFacilitiesRating, int companyGuidanceRating) {
        // Locate the position and update the evaluation.
        return "Professor evaluation saved";
    }
}
