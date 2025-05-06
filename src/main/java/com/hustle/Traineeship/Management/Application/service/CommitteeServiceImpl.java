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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    // Inject the necessary repositories and services
    // For the strategy pattern, you might have a factory that returns the correct strategy based on the parameter.

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private StudentsService studentsService;

    @Autowired // Added ProfessorService injection
    private ProfessorService professorService;

    @Override
    public List<Student> getApplicants() {
        // Returns students who do not have an assigned traineeship
        return studentsService.findStudentsWithoutTraineeship();
    }

    @Override
    public Student getApplicantByUniversityId(String universityId) {
        // Assuming StudentRepository has a method to find by universityId.
        // If universityId is unique, this should return a single Student or null/Optional.empty().
        // If universityId is not the primary key, ensure the repository method handles it correctly.
        return studentRepository.findByUniversityId(universityId)
                .orElse(null); // Or throw an exception if a student must always be found
    }

    @Override
    public List<TraineeshipPosition> searchPositionsForStudent(Long studentId, String strategy) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        List<String> studentInterests = student.getInterests();
        String studentPreferredLocation = student.getPreferredLocation();
        List<String> studentSkills = student.getSkills();

        List<TraineeshipPosition> availablePositions = traineeshipPositionRepository.getAvailableTraineeshipPositions();

        return availablePositions.stream()
                .filter(position -> {
                    // Skill matching (required for all strategies)
                    List<String> requiredSkills = position.getRequiredSkills();
                    boolean skillsMatch = requiredSkills == null || requiredSkills.isEmpty() || studentSkills.containsAll(requiredSkills);
                    if (!skillsMatch) {
                        return false;
                    }

                    // Strategy-based filtering
                    boolean interestMatch = studentInterests == null || studentInterests.isEmpty() ||
                                            position.getTopics() == null || position.getTopics().isEmpty() ||
                                            !Collections.disjoint(studentInterests, position.getTopics());

                    boolean locationMatch = studentPreferredLocation == null || studentPreferredLocation.isEmpty() ||
                                             position.getCompany() == null || position.getCompany().getLocation() == null ||
                                             position.getCompany().getLocation().equalsIgnoreCase(studentPreferredLocation);

                    switch (strategy.toLowerCase()) {
                        case "interests":
                            return interestMatch;
                        case "location":
                            return locationMatch;
                        case "both":
                            return interestMatch && locationMatch;
                        default:
                            return false; // Or throw an exception for invalid strategy
                    }
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

        // Assign student to position and position ID to student
        position.setStudent(student);
        student.setTraineeshipPosition(position); // Keep both sides of the relationship in sync in memory
        student.setTraineeshipId(position.getId());
        position.setStatus(TraineeshipStatus.IN_PROGRESS); // Set status to IN_PROGRESS

        // Ensure an Evaluation object exists for this traineeship position
        if (position.getEvaluation() == null) {
            Evaluation newEvaluation = new Evaluation();
            newEvaluation.setTraineeshipPosition(position); // Set the owning side of the relationship for FK
            position.setEvaluation(newEvaluation);          // Set the reference in the mappedBy side
        }

        traineeshipPositionRepository.save(position); // Position is the owner, save it first or ensure cascading is correct
        studentRepository.save(student); // Save student to persist traineeshipId and traineeshipPosition reference

        return "Traineeship position ID " + positionId + " successfully assigned to student " + student.getUsername() + ".";
    }

    @Override
    public String assignSupervisor(Long positionId, Long professorId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found with id: " + positionId));

        Professor professor = professorService.findProfessorById(professorId);
        // findProfessorById already throws if not found, so no null check needed here.

        position.setSupervisor(professor);
        traineeshipPositionRepository.save(position);

        return "Professor " + professor.getFullName() + " successfully assigned to traineeship position " + position.getDescription() + ".";
    }

    @Override
    public List<TraineeshipPosition> getInProgressTraineeships() {
        return traineeshipPositionRepository.findByStatus(TraineeshipStatus.IN_PROGRESS);
    }

    @Override
    @Transactional // Add transactional because we might update status
    public List<TraineeshipPosition> getFullyAssignedTraineeships() {
        List<TraineeshipPosition> positions = traineeshipPositionRepository.findByStudentIsNotNullAndSupervisorIsNotNullAndEndDateAfter(java.time.LocalDate.now());
        
        // Ensure that any "fully assigned" position with a null status is updated to IN_PROGRESS
        boolean needsSave = false;
        for (TraineeshipPosition position : positions) {
            if (position.getStatus() == null) {
                position.setStatus(TraineeshipStatus.IN_PROGRESS);
                // No need to save individually if the repository save is outside loop and covers all changes
                // However, saving each one ensures it, or collect and save all.
                // For simplicity here, let's assume the JPA context handles changes if the list is managed.
                // To be explicit and safe, we can save each one or save the list if the method allows.
                // Let's save each one that gets modified to be sure.
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
            // If no evaluation exists, create one, link it, and save.
            evaluation = new Evaluation();
            evaluation.setTraineeshipPosition(position); // Set the owning side
            position.setEvaluation(evaluation);          // Set the mappedBy side
            // Saving the position should cascade to save the new evaluation due to CascadeType.ALL
            traineeshipPositionRepository.save(position);
        }
        return evaluation;
    }
}
