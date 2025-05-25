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
    public Company createCompany(Company company) {
        companyRepository.save(company);
        return company;
    }

    @Override
    public void announcePosition(TraineeshipPosition position) {
        positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long positionId) {
        positionRepository.deleteById(positionId);
    }

    @Override
    public void evaluateTraineeship(Evaluation evaluation) {
        TraineeshipPosition position = evaluation.getTraineeshipPosition();
        if (position == null) {
            throw new RuntimeException("Traineeship position not specified in evaluation");
        }

        TraineeshipPosition existingPosition = positionRepository.findById(position.getId())
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + position.getId()));

        Evaluation existingEvaluation = existingPosition.getEvaluation();
        if (existingEvaluation != null) {
            // Update the existing evaluation with the new values.
            existingEvaluation.setMotivationRating(evaluation.getMotivationRating());
            existingEvaluation.setEffectivenessRating(evaluation.getEffectivenessRating());
            existingEvaluation.setEfficiencyRating(evaluation.getEfficiencyRating());
            existingEvaluation.setCompanyFacilitiesRating(evaluation.getCompanyFacilitiesRating());
            existingEvaluation.setCompanyGuidanceRating(evaluation.getCompanyGuidanceRating());
        } else {
            existingPosition.setEvaluation(evaluation);
        }

        positionRepository.save(existingPosition);
    }

    @Override
    public List<TraineeshipPosition> getAvailablePositions(Long companyId) {
        return positionRepository.findByCompanyId(companyId);
    }

    @Override
    public TraineeshipPosition getTraineeshipPositionById(Long positionId) {
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
        Company existingCompany = companyRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Company not found for username: " + username));

        existingCompany.setCompanyName(updatedCompany.getCompanyName());
        existingCompany.setLocation(updatedCompany.getLocation());

        companyRepository.save(existingCompany);
    }
}
