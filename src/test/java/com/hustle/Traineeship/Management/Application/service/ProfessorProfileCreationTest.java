package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.Student;
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

@ExtendWith({MockitoExtension.class})
class ProfessorProfileCreationTest {
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




}
