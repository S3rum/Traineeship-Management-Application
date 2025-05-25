package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentsService {

    Student createStudentProfile(Student student);

    Student findByUsername(String username);

    void updateStudentProfile(Student student, String username);

    String applyForTraineeship(Long studentId, Long positionId);

    Student findStudentById(Long studentId);

    List<TraineeshipLogBook> getTraineeshipLogbook(Long studentId);

    TraineeshipLogBook addLogEntry(Long studentId, TraineeshipLogBook book);

    TraineeshipLogBook findLogEntryById(Long entryId);

    void deleteLogEntry(Long entryId);

    void updateLogEntry(Long studentId, TraineeshipLogBook existingEntry);

    List<Student> findStudentsWithoutTraineeship();

    void setTraineeshipId(Long studentId);
}
