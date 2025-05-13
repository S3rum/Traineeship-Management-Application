package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.*;
import com.hustle.Traineeship.Management.Application.repos.CommitteeRepository;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeTest {

    @Mock
    TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CommitteeRepository committeeRepository;

    @Mock
    StudentsService studentsService;

    @Mock
    ProfessorService professorService;

    @InjectMocks
    CommitteeServiceImpl committeeServiceImpl;

    @Test
    void testGetApplicants() {
        // Arrange
        Student student1 = new Student();
        student1.setId(1L);
        student1.setUsername("student1");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setUsername("student2");// Arrange

        List<Student> expectedApplicants = Arrays.asList(student1, student2);
        when(studentsService.findStudentsWithoutTraineeship()).thenReturn(expectedApplicants);

        // Act
        List<Student> actualApplicants = committeeServiceImpl.getApplicants();

        // Assert
        assertEquals(expectedApplicants, actualApplicants);
        verify(studentsService, times(1)).findStudentsWithoutTraineeship();
    }

    @Test
    void testSearchPositionsForStudent() {
        // Arrange
        Long studentId = 1L;
        String strategy = "both";

        Student student = new Student();
        student.setId(studentId);
        student.setSkills(Arrays.asList("Java", "Spring Boot"));
        student.setInterests(Arrays.asList("Software Development", "Web Development"));
        student.setPreferredLocation("New York");

        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setRequiredSkills(Arrays.asList("Java", "Spring Boot"));
        position1.setTopics(Arrays.asList("Software Development"));
        position1.setCompany(new Company(null, null, null, "TechCorp", "New York"));

        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setRequiredSkills(Arrays.asList("Python"));
        position2.setTopics(Arrays.asList("Data Science"));
        position2.setCompany(new Company(null, null, null, "TechCorp", "New York"));

        TraineeshipPosition position3 = new TraineeshipPosition();
        position3.setId(3L);
        position3.setRequiredSkills(Arrays.asList("Java"));
        position3.setTopics(Arrays.asList("Web Development"));
        position3.setCompany(new Company(null, null, null, "WebCorp", "New York"));

        List<TraineeshipPosition> availablePositions = Arrays.asList(position1, position2, position3);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.getAvailableTraineeshipPositions()).thenReturn(availablePositions);

        // Act
        List<TraineeshipPosition> result = committeeServiceImpl.searchPositionsForStudent(studentId, strategy);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(position1));
        assertTrue(result.contains(position3));
        assertFalse(result.contains(position2));

        verify(studentRepository, times(1)).findById(studentId);
        verify(traineeshipPositionRepository, times(1)).getAvailableTraineeshipPositions();
    }
    @Test
    void testAssignTraineeship() {
        // Arrange
        Long studentId = 1L;
        Long positionId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setUsername("student1");

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setDescription("Software Development Traineeship");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        // Act
        String result = committeeServiceImpl.assignTraineeship(studentId, positionId);

        // Assert
        assertEquals("Traineeship position ID 1 successfully assigned to student student1.", result);
        assertEquals(position, student.getTraineeshipPosition());
        assertEquals(student, position.getStudent());
        assertEquals(TraineeshipStatus.IN_PROGRESS, position.getStatus());

        verify(studentRepository, times(1)).findById(studentId);
        verify(traineeshipPositionRepository, times(1)).findById(positionId);
        verify(traineeshipPositionRepository, times(1)).save(position);
        verify(studentRepository, times(1)).save(student);
    }
    @Test
    void testAssignSupervisorToTraineeship() {
        // Arrange
        Long positionId = 1L;
        Long professorId = 1L;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setStatus(TraineeshipStatus.IN_PROGRESS);
        position.setDescription("Software Development Traineeship");

        Professor professor = new Professor();
        professor.setId(professorId);
        professor.setFullName("Dr. Smith");
        professor.setInterests(Arrays.asList("Software Development", "Web Development"));
        professor.setSupervisedPositions(Arrays.asList(new TraineeshipPosition(), new TraineeshipPosition())); // Simulate 2 supervised traineeships

        when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));
        when(professorService.findProfessorById(professorId)).thenReturn(professor);

        // Act
        String result = committeeServiceImpl.assignSupervisor(positionId, professorId);

        // Assert
        assertEquals("Professor Dr. Smith successfully assigned to traineeship position Software Development Traineeship.", result);
        assertEquals(professor, position.getSupervisor());
        verify(traineeshipPositionRepository, times(1)).findById(positionId);
        verify(professorService, times(1)).findProfessorById(professorId);
        verify(traineeshipPositionRepository, times(1)).save(position);
    }
    @Test
    void testGetInProgressTraineeships() {
        // Arrange
        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setStatus(TraineeshipStatus.IN_PROGRESS);

        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setStatus(TraineeshipStatus.IN_PROGRESS);

        List<TraineeshipPosition> inProgressPositions = Arrays.asList(position1, position2);

        when(traineeshipPositionRepository.findByStatus(TraineeshipStatus.IN_PROGRESS)).thenReturn(inProgressPositions);

        // Act
        List<TraineeshipPosition> result = committeeServiceImpl.getInProgressTraineeships();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(position1));
        assertTrue(result.contains(position2));
        verify(traineeshipPositionRepository, times(1)).findByStatus(TraineeshipStatus.IN_PROGRESS);
    }

    @Test
    void testFinalizeTraineeshipBasedOnEvaluations() {
        // Arrange
        Long positionId = 1L;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setStatus(TraineeshipStatus.IN_PROGRESS);

        Evaluation evaluation = new Evaluation();
        evaluation.setMotivationRating(8); // Example score to determine pass
        evaluation.setEffectivenessRating(9);
        evaluation.setEfficiencyRating(7);
        position.setEvaluation(evaluation);

        when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));


        String resultPass = committeeServiceImpl.finalizeTraineeship(positionId, true);

        assertEquals("Traineeship position ID 1 finalized as PASS.", resultPass);
        assertEquals(TraineeshipStatus.COMPLETED_PASS, position.getStatus());

        position.setStatus(TraineeshipStatus.IN_PROGRESS);
        String resultFail = committeeServiceImpl.finalizeTraineeship(positionId, false);

        assertEquals("Traineeship position ID 1 finalized as FAIL.", resultFail);
        assertEquals(TraineeshipStatus.COMPLETED_FAIL, position.getStatus());

        verify(traineeshipPositionRepository, times(2)).findById(positionId);
        verify(traineeshipPositionRepository, times(2)).save(position);
    }
}