package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;

public interface CompanyService {
    Company findByUsername(String username);
    void updateCompanyProfile(Company updatedCompany, String username);
    void createCompany(Company company);
    void announcePosition(TraineeshipPosition position);
    void deletePosition(Long positionId);
    void evaluateTraineeship(Evaluation evaluation);

    List<TraineeshipPosition> getAvailablePositions(Long companyId);

    TraineeshipPosition getTraineeshipPositionById(Long positionId);
    // Additional methods as needed
}
