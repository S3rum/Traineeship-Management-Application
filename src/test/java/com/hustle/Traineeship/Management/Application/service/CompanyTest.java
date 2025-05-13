package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Company;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.CompanyRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CompanyTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    TraineeshipPositionRepository traineeshipPositionRepository;

    @InjectMocks
    CompanyServiceImpl service;

    @Test
    void companyProfileCreation() {
        Company company = new Company();
        company.setCompanyName("Test Company");
        company.setLocation("Test Location");

        Mockito.when(companyRepository.save(Mockito.any(Company.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Company createdCompany = service.createCompany(company);

        Assertions.assertEquals(company.getCompanyName(), createdCompany.getCompanyName());
        Assertions.assertEquals(company.getLocation(), createdCompany.getLocation());
    }
    @Test
    void retrievesAvailableTraineeshipPositionsForCompany() {
        Long companyId = 1L;

        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        mockCompany.setCompanyName("Test Company");

        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setDescription("Software Developer Intern");
        position1.setCompany(mockCompany);

        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setDescription("Data Analyst Intern");
        position2.setCompany(mockCompany);

        List<TraineeshipPosition> positions = List.of(position1, position2);

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        Mockito.when(traineeshipPositionRepository.findByCompanyId(companyId)).thenReturn(positions);

        List<TraineeshipPosition> result = service.getAvailablePositions(companyId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(position1));
        Assertions.assertTrue(result.contains(position2));
    }
    @Test
    void retrievesAssignedTraineeshipPositionsForCompany() {
        Long companyId = 1L;

        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        mockCompany.setCompanyName("Test Company");

        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setDescription("Software Developer Intern");
        position1.setCompany(mockCompany);
        position1.setStudent(new Student()); // Assigned to a student

        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setDescription("Data Analyst Intern");
        position2.setCompany(mockCompany);
        position2.setStudent(new Student()); // Assigned to a student

        List<TraineeshipPosition> assignedPositions = List.of(position1, position2);

        Mockito.doReturn(assignedPositions)
                .when(traineeshipPositionRepository)
                .findByCompanyId(companyId);

        List<TraineeshipPosition> result = service.getAvailablePositions(companyId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(position1));
        Assertions.assertTrue(result.contains(position2));
    }
    @Test
    void announcesTraineeshipPositionSuccessfully() {
        Long companyId = 1L;

        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        mockCompany.setCompanyName("Test Company");

        TraineeshipPosition position = new TraineeshipPosition();
        position.setStartDate(LocalDate.of(2024, 1, 1));
        position.setEndDate(LocalDate.of(2024, 6, 30));
        position.setDescription("Develop a web application for internal use.");
        position.setRequiredSkills(List.of("Java", "Spring Boot", "SQL"));
        position.setTopics(List.of("Web Development", "Database Design"));
        position.setCompany(mockCompany);

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        Mockito.when(traineeshipPositionRepository.save(Mockito.any(TraineeshipPosition.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.announcePosition(position);

        Mockito.verify(traineeshipPositionRepository).save(position);
        Assertions.assertEquals(mockCompany, position.getCompany());
        Assertions.assertEquals("Develop a web application for internal use.", position.getDescription());
        Assertions.assertEquals(List.of("Java", "Spring Boot", "SQL"), position.getRequiredSkills());
        Assertions.assertEquals(List.of("Web Development", "Database Design"), position.getTopics());
    }
    @Test
    void deletesTraineeshipPositionSuccessfully() {
        Long companyId = 1L;
        Long positionId = 1L;

        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        mockCompany.setCompanyName("Test Company");

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setCompany(mockCompany);

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        Mockito.when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        service.deletePosition(positionId);

        Mockito.verify(traineeshipPositionRepository).deleteById(positionId);
    }
    @Test
    void fillsInEvaluationForTraineeshipSuccessfully() {
        Long companyId = 1L;
        Long positionId = 1L;

        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        mockCompany.setCompanyName("Test Company");

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setCompany(mockCompany);

        Evaluation evaluation = new Evaluation();
        evaluation.setMotivationRating(4);
        evaluation.setEffectivenessRating(5);
        evaluation.setEfficiencyRating(3);
        evaluation.setTraineeshipPosition(position);

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        Mockito.when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        service.evaluateTraineeship(evaluation);

        Mockito.verify(traineeshipPositionRepository).save(position);
        Assertions.assertEquals(4, evaluation.getMotivationRating());
        Assertions.assertEquals(5, evaluation.getEffectivenessRating());
        Assertions.assertEquals(3, evaluation.getEfficiencyRating());
        Assertions.assertEquals(position, evaluation.getTraineeshipPosition());
    }
}