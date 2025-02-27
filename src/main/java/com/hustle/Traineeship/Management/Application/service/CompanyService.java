package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;

public interface CompanyService {
    Company findByUsername(String username);
    boolean companyProfileExists(Long userId);

    Company createCompany(Company company);
    TraineeshipPosition announcePosition(TraineeshipPosition position);
    void deletePosition(Long positionId);
    String evaluateTraineeship(Long positionId, int motivationRating, int effectivenessRating, int efficiencyRating);
    List<TraineeshipPosition> getAvailablePositions(Long companyId);
    // Additional methods as needed
}
