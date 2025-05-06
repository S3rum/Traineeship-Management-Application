package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.repos.CommitteeRepository;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

        traineeshipPositionRepository.save(position); // Position is the owner, save it first or ensure cascading is correct
        studentRepository.save(student); // Save student to persist traineeshipId and traineeshipPosition reference

        return "Traineeship position ID " + positionId + " successfully assigned to student " + student.getUsername() + ".";
    }

    @Override
    public String assignSupervisor(Long positionId, String strategy) {
        // Use the appropriate SupervisorAssignmentStrategy (e.g., interests matching or minimum load)
        return "Supervisor assigned";
    }

    @Override
    public List<?> getInProgressTraineeships() {
        // Retrieve traineeships that are currently in progress.
        return Collections.emptyList();
    }

    @Override
    public String finalizeTraineeship(Long positionId, boolean pass) {
        // Complete the traineeship process with a pass or fail status.
        return "Traineeship finalized";
    }

    @Override
    public Committee findByUsername(String username) {
        return committeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Committee not found for username: " + username));
    }
}
