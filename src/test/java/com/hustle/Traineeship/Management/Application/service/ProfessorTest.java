package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.ProfessorRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith({MockitoExtension.class})
class ProfessorTest {
    @Mock
    ProfessorRepository professorRepository;
    @Mock
    TraineeshipPositionRepository traineeshipPositionRepository;
    @InjectMocks
    ProfessorServiceImpl service;
    @Test
    void professorProfileCreation() {
        Professor professor= new Professor();
        professor.setFullName("John");
        List<String> interests = List.of("Lava", "Volcanoes");
        professor.setInterests(interests);
        Mockito.when(professorRepository.save(Mockito.any(Professor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Professor professorToCreate = service.createProfessorProfile(professor);
        //Mockito.verify(professorRepository).save(professor);
        Assertions.assertEquals(professor.getFullName(),professorToCreate.getFullName());
        Assertions.assertEquals(professor.getInterests(),professorToCreate.getInterests());
    }
    @Test
    void retrievesSupervisedTraineeshipPositionsForProfessor() {
        Long professorId = 1L;

        Professor mockProfessor = new Professor();
        mockProfessor.setId(professorId);
        mockProfessor.setFullName("Dr. Smith");

        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setDescription("AI Research Intern");
        position1.setSupervisor(mockProfessor);

        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setDescription("Data Science Intern");
        position2.setSupervisor(mockProfessor);

        List<TraineeshipPosition> supervisedPositions = List.of(position1, position2);

        Mockito.when(traineeshipPositionRepository.findBySupervisorId(professorId))
                .thenReturn(supervisedPositions);

        List<TraineeshipPosition> result = service.getSupervisedPositions(professorId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(position1));
        Assertions.assertTrue(result.contains(position2));

    }
    @Test
    void fillsInEvaluationForSupervisedTraineeshipSuccessfully() {
        Long professorId = 1L;
        Long positionId = 1L;

        Professor mockProfessor = new Professor();
        mockProfessor.setId(professorId);
        mockProfessor.setFullName("Dr. Smith");

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setSupervisor(mockProfessor);

        Evaluation evaluation = new Evaluation();
        evaluation.setMotivationRating(4);
        evaluation.setEffectivenessRating(5);
        evaluation.setEfficiencyRating(3);
        evaluation.setCompanyFacilitiesRating(4);
        evaluation.setCompanyGuidanceRating(5);
        evaluation.setTraineeshipPosition(position);

        Mockito.when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        service.evaluateTraineeship(evaluation);

        Mockito.verify(traineeshipPositionRepository).save(position);
        Assertions.assertEquals(4, evaluation.getMotivationRating());
        Assertions.assertEquals(5, evaluation.getEffectivenessRating());
        Assertions.assertEquals(3, evaluation.getEfficiencyRating());
        Assertions.assertEquals(4, evaluation.getCompanyFacilitiesRating());
        Assertions.assertEquals(5, evaluation.getCompanyGuidanceRating());
        Assertions.assertEquals(position, evaluation.getTraineeshipPosition());
    }



}
