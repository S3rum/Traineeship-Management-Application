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
    public Student updateStudentProfile(Student updatedStudent, String username) {
        Student existingStudent = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));

        existingStudent.setFullName(updatedStudent.getFullName());
        existingStudent.setUniversityId(updatedStudent.getUniversityId());
        existingStudent.setInterests(updatedStudent.getInterests());
        existingStudent.setSkills(updatedStudent.getSkills());
        existingStudent.setPreferredLocation(updatedStudent.getPreferredLocation());

        return studentRepository.save(existingStudent);
    }

    @Override
    public String applyForTraineeship(Long studentId, Long positionId) {
        // Retrieve the student from the database
        Student student = findStudentById(studentId);

        // Retrieve the traineeship position using its id
        TraineeshipPosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found for id: " + positionId));

        // Check if the position is already assigned to a student.
        if (position.getStudent() != null) {
            return "This traineeship position is already taken.";
        }

        // Assign the student to the traineeship position
        position.setStudent(student);

        // Save the updated position, which will update the foreign key (student_id) in the database
        positionRepository.save(position);

        return "Application successful";
    }


    // Optionally, you can remove or repurpose this method.
    @Override
    public String updateLogbook(Long studentId, String entries) {
        return "Logbook updated";
    }


    @Override
    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found for studentId: " + studentId));
    }

    @Override
    public List<TraineeshipPosition> getAvailableTraineeshipPositions() {
        return positionRepository.getAvailableTraineeshipPositions();
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
    public TraineeshipLogBook updateLogEntry(Long studentId, TraineeshipLogBook entry) {
        // Here, you can ensure the student is set and then update.
        Student student = findStudentById(studentId);
        entry.setStudent(student);
        return logBookRepository.save(entry);
    }

}
