package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Evaluation;
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
    public void updateProfessorProfile(Professor updatedProfessor, String username) {
        Professor existingProfessor = professorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Professor not found for username: " + username));

        existingProfessor.setFullName(updatedProfessor.getFullName());
        existingProfessor.setInterests(updatedProfessor.getInterests());

        professorRepository.save(existingProfessor);
    }

    @Override
    public List<TraineeshipPosition> getSupervisedPositions(Long professorId) {
        return positionRepository.findBySupervisorId(professorId);
    }

    @Override
    public void evaluateTraineeship(Evaluation evaluation) {
        TraineeshipPosition position = evaluation.getTraineeshipPosition();
        if (position == null) {
            throw new RuntimeException("Traineeship position not specified in evaluation");
        }

        TraineeshipPosition existingPosition = positionRepository.findById(position.getId())
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        Evaluation existingEvaluation = existingPosition.getEvaluation();
        if (existingEvaluation != null) {
            existingEvaluation.setProfessorMotivationRating(evaluation.getProfessorMotivationRating());
            existingEvaluation.setProfessorEffectivenessRating(evaluation.getProfessorEffectivenessRating());
            existingEvaluation.setProfessorEfficiencyRating(evaluation.getProfessorEfficiencyRating());
            existingEvaluation.setCompanyFacilitiesRating(evaluation.getCompanyFacilitiesRating());
            existingEvaluation.setCompanyGuidanceRating(evaluation.getCompanyGuidanceRating());
        } else {
            existingPosition.setEvaluation(evaluation);
        }
        positionRepository.save(existingPosition);

    }
    public Professor findByUsername(String username) {
        return professorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Professor not found for username: " + username));
    }

    @Override
    public TraineeshipPosition findTraineeshipPositionById(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("TraineeshipPosition not found for ID: " + positionId));
    }

    @Override
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    @Override
    public Professor findProfessorById(Long professorId) {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found for ID: " + professorId));
    }
}


