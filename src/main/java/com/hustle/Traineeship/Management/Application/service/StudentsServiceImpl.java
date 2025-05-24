package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipLogBookRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("studentsService")
public class StudentsServiceImpl implements StudentsService {

    @Autowired
    private TraineeshipLogBookRepository logBookRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TraineeshipPositionRepository positionRepository;

    @Override
    public Student createStudentProfile(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));
    }

    @Override
    public void updateStudentProfile(Student updatedStudent, String username) {
        Student existingStudent = findByUsername(username);
        existingStudent.setFullName(updatedStudent.getFullName());
        existingStudent.setUniversityId(updatedStudent.getUniversityId());
        existingStudent.setInterests(updatedStudent.getInterests());
        existingStudent.setSkills(updatedStudent.getSkills());
        existingStudent.setPreferredLocation(updatedStudent.getPreferredLocation());
        existingStudent.setUniversityIdFromId();

        studentRepository.save(existingStudent);
    }

    @Override
    public String applyForTraineeship(Long studentId, Long positionId) {
        Student student = findStudentById(studentId);

        TraineeshipPosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found for id: " + positionId));

        if (position.getStudent() != null) {
            return "This traineeship position is already taken.";
        }

        position.setStudent(student);
        positionRepository.save(position);
        return "Application successful";
    }

    @Override
    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found for studentId: " + studentId));
    }

    @Override
    public List<TraineeshipLogBook> getTraineeshipLogbook(Long studentId) {
        return logBookRepository.findByStudentId(studentId);
    }

    @Override
    public TraineeshipLogBook addLogEntry(Long studentId, TraineeshipLogBook entry) {
        Student student = findStudentById(studentId);
        entry.setStudent(student);
        return logBookRepository.save(entry);
    }

    @Override
    public TraineeshipLogBook findLogEntryById(Long entryId) {
        return logBookRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Log entry not found for id: " + entryId));
    }

    @Override
    public void deleteLogEntry(Long entryId) {
        logBookRepository.deleteById(entryId);
    }

    @Override
    public void updateLogEntry(Long studentId, TraineeshipLogBook entry) {
        Student student = findStudentById(studentId);
        entry.setStudent(student);
        logBookRepository.save(entry);
    }

    @Override
    public List<Student> findStudentsWithoutTraineeship() {
        return studentRepository.findByTraineeshipIdIs(0L);
    }

    @Override
    public void setTraineeshipId(Long studentId) {
        Student student = findStudentById(studentId);
        student.setTraineeshipPosition(null);
        student.setTraineeshipId(0L); // Use 0L
        studentRepository.save(student);
    }
}
