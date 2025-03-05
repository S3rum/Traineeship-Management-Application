package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.CompanyRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TraineeshipPositionRepository positionRepository;

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void announcePosition(TraineeshipPosition position) {
        // Additional business logic can be added here if needed.
        positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long positionId) {
        // Delete the position by its ID.
        positionRepository.deleteById(positionId);
    }

    // Refactored method that uses an Evaluation object.
    @Override
    public void evaluateTraineeship(Evaluation evaluation) {
        // Retrieve the associated traineeship position from the evaluation.
        TraineeshipPosition position = evaluation.getTraineeshipPosition();
        if (position == null) {
            throw new RuntimeException("Traineeship position not specified in evaluation");
        }

        // Retrieve the current position from the database.
        TraineeshipPosition existingPosition = positionRepository.findById(position.getId())
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + position.getId()));

        // Check if an evaluation already exists for this position.
        Evaluation existingEvaluation = existingPosition.getEvaluation();
        if (existingEvaluation != null) {
            // Update the existing evaluation with the new values.
            existingEvaluation.setMotivationRating(evaluation.getMotivationRating());
            existingEvaluation.setEffectivenessRating(evaluation.getEffectivenessRating());
            existingEvaluation.setEfficiencyRating(evaluation.getEfficiencyRating());
            existingEvaluation.setCompanyFacilitiesRating(evaluation.getCompanyFacilitiesRating());
            existingEvaluation.setCompanyGuidanceRating(evaluation.getCompanyGuidanceRating());

        } else {
            // Associate the new evaluation with the position.
            existingPosition.setEvaluation(evaluation);
        }

        // Save the position so that the evaluation is persisted (assuming proper cascade settings).
        positionRepository.save(existingPosition);
    }

    @Override
    public List<TraineeshipPosition> getAvailablePositions(Long companyId) {
        // Retrieve positions that are not yet assigned to a student.
        return positionRepository.findByCompanyId(companyId);
    }

    @Override
    public TraineeshipPosition getTraineeshipPositionById(Long positionId) {
        // Retrieve the traineeship position by ID or throw an exception if not found.
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + positionId));
    }


    @Override
    public Company findByUsername(String username) {
        return companyRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Company not found for username: " + username));
    }

    @Override
    public void updateCompanyProfile(Company updatedCompany, String username) {
        // Load the existing company by username.
        Company existingCompany = companyRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Company not found for username: " + username));

        // Update the necessary fields.
        existingCompany.setCompanyName(updatedCompany.getCompanyName());
        existingCompany.setLocation(updatedCompany.getLocation());
        // If there are additional fields to update, add them here.

        // Save the updated company to the database.
        companyRepository.save(existingCompany);
    }
}
