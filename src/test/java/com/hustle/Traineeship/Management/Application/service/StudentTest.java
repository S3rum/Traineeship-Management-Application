package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipLogBookRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith({MockitoExtension.class})
class StudentTest {
    @Mock
    TraineeshipLogBookRepository logBookRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    TraineeshipPositionRepository traineeshipPositionRepository;
    @InjectMocks
    StudentsServiceImpl service;

    @Test
    void testFindStudentsWithoutTraineeship() {
        // Arrange
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFullName("John Doe");
        student1.setTraineeshipId(0L);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFullName("Jane Smith");
        student2.setTraineeshipId(0L);

        List<Student> studentsWithoutTraineeship = List.of(student1, student2);

        Mockito.when(studentRepository.findByTraineeshipIdIs(0L)).thenReturn(studentsWithoutTraineeship);

        // Act
        List<Student> result = service.findStudentsWithoutTraineeship();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(student1));
        Assertions.assertTrue(result.contains(student2));
        Mockito.verify(studentRepository, Mockito.times(1)).findByTraineeshipIdIs(0L);
    }
    @Test
    void studentProfileCreation() {
        Student student = new Student();
        student.setFullName("John");
        List<String> interests = List.of("Lava", "Volcanoes");
        student.setUniversityId("University ID Test");
        student.setSkills(List.of("Skill1", "Skill2"));
        student.setPreferredLocation("Location Test");
        //student.setTraineeshipPosition(null);
        //student.setTraineeshipId(null);
        //student.setUniversityIdFromId();
        //student.setTraineeshipId(null);
        //student.setTraineeshipPosition(null);
        student.setInterests(interests);
        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Student studentToCreate = service.createStudentProfile(student);
        //Mockito.verify(studentRepository).save(student);
        Assertions.assertEquals(student.getFullName(), studentToCreate.getFullName());
        Assertions.assertEquals(student.getInterests(), studentToCreate.getInterests());
        Assertions.assertEquals(student.getUniversityId(), studentToCreate.getUniversityId());
        Assertions.assertEquals(student.getSkills(), studentToCreate.getSkills());
        Assertions.assertEquals(student.getPreferredLocation(), studentToCreate.getPreferredLocation());
        //Assertions.assertEquals(student.getTraineeshipPosition(),studentToCreate.getTraineeshipPosition());

    }
    @Test
    void studentApplyForTraineeship() {
        Long studentId = 1L;
        Long positionId = 1L;

        Student mockStudent = new Student();
        mockStudent.setTraineeshipId(studentId);

        TraineeshipPosition mockPosition = new TraineeshipPosition();
        mockPosition.setId(positionId);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));
        Mockito.when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(mockPosition));

        String result = service.applyForTraineeship(studentId, positionId);
        Assertions.assertEquals("Application successful", result);
        Assertions.assertEquals(positionId, mockStudent.getTraineeshipId());
    }
    @Test
    void addLogEntryForAssignedTraineeship() {
        Long studentId = 1L;
        TraineeshipLogBook logEntry = new TraineeshipLogBook();
        logEntry.setDescription("Worked on project tasks.");
        logEntry.setStartDate(LocalDate.now());
        logEntry.setEndDate(LocalDate.now().plusDays(1));

        Student mockStudent = new Student();
        mockStudent.setTraineeshipId(studentId);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));
        Mockito.when(logBookRepository.save(Mockito.any(TraineeshipLogBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TraineeshipLogBook savedLogEntry = service.addLogEntry(studentId, logEntry);

        Assertions.assertEquals(logEntry.getDescription(), savedLogEntry.getDescription());
        Assertions.assertEquals(logEntry.getStartDate(), savedLogEntry.getStartDate());
        Assertions.assertEquals(logEntry.getEndDate(), savedLogEntry.getEndDate());
        Assertions.assertEquals(mockStudent, savedLogEntry.getStudent());
    }

}







