package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.Evaluation;
import com.hustle.Traineeship.Management.Application.model.TraineeshipStatus;
import com.hustle.Traineeship.Management.Application.repos.CommitteeRepository;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private ProfessorService professorService;

    @Override
    public List<Student> getApplicants() {
        // Returns students who do not have an assigned traineeship
        return studentsService.findStudentsWithoutTraineeship();
    }

    @Override
    public Student getApplicantByUniversityId(String universityId) {
        return studentRepository.findByUniversityId(universityId)
                .orElse(null);
    }

    @Override
    public List<TraineeshipPosition> searchPositionsForStudent(Long studentId, String strategy) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<TraineeshipPosition> availablePositions = traineeshipPositionRepository.findByStudentIsNull();

        return availablePositions.stream()
                .filter(position -> {
                    List<String> studentInterests = student.getInterests();
                    String studentLocation = student.getPreferredLocation();
                    List<String> studentSkills = student.getSkills();

                    List<String> positionTopics = position.getTopics();
                    String companyLocation = position.getCompany() != null ? position.getCompany().getLocation() : null;
                    List<String> requiredSkills = position.getRequiredSkills();

                    boolean interestMatch = (studentInterests == null || studentInterests.isEmpty() ||
                            positionTopics == null || positionTopics.isEmpty() ||
                            studentInterests.stream().anyMatch(positionTopics::contains));

                    boolean locationMatch = (studentLocation == null || studentLocation.trim().isEmpty() ||
                            companyLocation == null || companyLocation.trim().isEmpty() ||
                            studentLocation.trim().equalsIgnoreCase(companyLocation.trim()));

                    boolean skillsMatch = (studentSkills == null || studentSkills.isEmpty() ||
                            requiredSkills == null || requiredSkills.isEmpty() ||
                            studentSkills.stream().anyMatch(requiredSkills::contains));

                    return switch (strategy.toLowerCase()) {
                        case "interests" -> interestMatch;
                        case "location" -> locationMatch;
                        case "both" -> interestMatch && locationMatch;
                        case "skills" -> skillsMatch;
                        case "all" -> interestMatch && locationMatch && skillsMatch;
                        default -> false;
                    };
                })
                .collect(Collectors.toList());
    }

    @Override
    public String assignTraineeship(Long studentId, Long positionId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + positionId));

        if (position.getStudent() != null) {
            return "Error: Traineeship position with ID " + positionId + " is already assigned to student " + position.getStudent().getUsername();
        }

        if (student.getTraineeshipPosition() != null) {
            return "Error: Student " + student.getUsername() + " is already assigned to position ID " + student.getTraineeshipPosition().getId();
        }

        position.setStudent(student);
        student.setTraineeshipPosition(position);
        student.setTraineeshipId(position.getId());
        position.setStatus(TraineeshipStatus.IN_PROGRESS);

        if (position.getEvaluation() == null) {
            Evaluation newEvaluation = new Evaluation();
            newEvaluation.setTraineeshipPosition(position);
            position.setEvaluation(newEvaluation);
        }

        traineeshipPositionRepository.save(position);
        studentRepository.save(student);

        return "Traineeship position ID " + positionId + " successfully assigned to student " + student.getUsername() + ".";
    }

    @Override
    public String assignSupervisor(Long positionId, Long professorId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + positionId));

        Professor professor = professorService.findProfessorById(professorId);

        position.setSupervisor(professor);
        traineeshipPositionRepository.save(position);

        return "Professor " + professor.getFullName() + " successfully assigned to traineeship position " + position.getDescription() + ".";
    }

    @Override
    public List<TraineeshipPosition> getInProgressTraineeships() {
        return traineeshipPositionRepository.findByStatus(TraineeshipStatus.IN_PROGRESS);
    }

    @Override
    @Transactional
    public List<TraineeshipPosition> getFullyAssignedTraineeships() {
        List<TraineeshipPosition> positions = traineeshipPositionRepository.findByStudentIsNotNullAndSupervisorIsNotNullAndEndDateAfter(java.time.LocalDate.now());

        for (TraineeshipPosition position : positions) {
            if (position.getStatus() == null) {
                position.setStatus(TraineeshipStatus.IN_PROGRESS);
                traineeshipPositionRepository.save(position);
            }
        }
        return positions;
    }

    @Override
    public String finalizeTraineeship(Long positionId, boolean pass) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + positionId));

        if (pass) {
            position.setStatus(TraineeshipStatus.COMPLETED_PASS);
        } else {
            position.setStatus(TraineeshipStatus.COMPLETED_FAIL);
        }
        traineeshipPositionRepository.save(position);
        return "Traineeship position ID " + positionId + " finalized as " + (pass ? "PASS" : "FAIL") + ".";
    }

    @Override
    public Committee findByUsername(String username) {
        return committeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Committee not found for username: " + username));
    }

    @Override
    @Transactional
    public Evaluation getEvaluationForTraineeship(Long positionId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + positionId));
        
        Evaluation evaluation = position.getEvaluation();
        
        if (evaluation == null) {
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position);
            position.setEvaluation(evaluation);
            traineeshipPositionRepository.save(position);
        }
        return evaluation;
    }
}
