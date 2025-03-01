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
        // Load the existing professor by username
        Professor existingProfessor = professorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Professor not found for username: " + username));

        // Update the necessary fields
        existingProfessor.setFullName(updatedProfessor.getFullName());
        existingProfessor.setInterests(updatedProfessor.getInterests());
        // If there are additional fields to update, add them here

        // Save the updated professor to the database
        professorRepository.save(existingProfessor);
    }

    @Override
    public List<TraineeshipPosition> getSupervisedPositions(Long professorId) {
        // Retrieve positions supervised by the professor.
        return positionRepository.findBySupervisorId(professorId);
    }

    @Override
    public void evaluateTraineeship(Evaluation evaluation) {
        // Retrieve the traineeship position from the evaluation object
        TraineeshipPosition position = evaluation.getTraineeshipPosition();
        if (position == null) {
            throw new RuntimeException("Traineeship position not specified in evaluation");
        }

        // Load the current state of the position from the database (including its evaluation if any)
        TraineeshipPosition existingPosition = positionRepository.findById(position.getId())
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        // Check if an evaluation already exists for this position
        Evaluation existingEvaluation = existingPosition.getEvaluation();
        if (existingEvaluation != null) {
            // Update the existing evaluation fields
            existingEvaluation.setProfessorMotivationRating(evaluation.getProfessorMotivationRating());
            existingEvaluation.setProfessorEffectivenessRating(evaluation.getProfessorEffectivenessRating());
            existingEvaluation.setProfessorEfficiencyRating(evaluation.getProfessorEfficiencyRating());
            existingEvaluation.setCompanyFacilitiesRating(evaluation.getCompanyFacilitiesRating());
            existingEvaluation.setCompanyGuidanceRating(evaluation.getCompanyGuidanceRating());
        } else {
            // Associate the new evaluation with the position
            existingPosition.setEvaluation(evaluation);
        }

        // Save the position, cascading the evaluation save/update
        positionRepository.save(existingPosition);

    }
    @Override
    public boolean professorProfileExists(Long userId) {
        return professorRepository.existsByUserId(userId);
    }
    public Professor findByUsername(String username) {
        return professorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Professor not found for username: " + username));
    }
}


