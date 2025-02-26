package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.Student;
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
    public Professor updateProfessorProfile(Professor updatedProfessor, String username) {
        // Load the existing professor by username
        Professor existingProfessor = professorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Professor not found for username: " + username));

        // Update the necessary fields
        existingProfessor.setFullName(updatedProfessor.getFullName());
        existingProfessor.setInterests(updatedProfessor.getInterests());
        // If there are additional fields to update, add them here

        // Save the updated professor to the database
        return professorRepository.save(existingProfessor);
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
    @Override
    public boolean professorProfileExists(Long userId) {
        return professorRepository.existsByUserId(userId);
    }
    public Professor findByUsername(String username) {
        return professorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));
    }
}


