package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.Committee;
import com.hustle.Traineeship.Management.Application.repos.CommitteeRepository;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    // Inject the necessary repositories and services
    // For the strategy pattern, you might have a factory that returns the correct strategy based on the parameter.

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Autowired
    private CommitteeRepository committeeRepository;

    @Override
    public List<Student> getApplicants() {
        // Find all positions with a non-null student (i.e., applied)
        return traineeshipPositionRepository.findAll().stream()
                .map(TraineeshipPosition::getStudent)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<TraineeshipPosition> searchPositionsForStudent(Long studentId, String strategy) {
        // Based on the strategy parameter (e.g., "interests", "location", "combined")
        // instantiate and use the corresponding PositionAssignmentStrategy to filter positions.
        // For now, return an empty list.
        return Collections.emptyList();
    }

    @Override
    public String assignTraineeship(Long studentId, Long positionId) {
        // Link the student with the position and update the state.
        return "Traineeship assigned";
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
