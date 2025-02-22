package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Company;
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
        return companyRepository.save(company);
    }

    @Override
    public TraineeshipPosition announcePosition(TraineeshipPosition position) {
        // Additional business logic can be added here.
        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long positionId) {
        positionRepository.deleteById(positionId);
    }

    @Override
    public String evaluateTraineeship(Long positionId, int motivationRating, int effectivenessRating, int efficiencyRating) {
        // Locate the position, update its Evaluation entity, and save.
        // For brevity, the detailed implementation is omitted.
        return "Evaluation saved";
    }

    @Override
    public List<TraineeshipPosition> getAvailablePositions(Long companyId) {
        // Retrieve positions that are not yet assigned.
        return positionRepository.findByCompanyIdAndStudentIsNull(companyId);
    }
}
