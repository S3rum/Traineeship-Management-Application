package com.hustle.Traineeship.Management.Application.strategy;

import com.hustle.Traineeship.Management.Application.model.Professor;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;

public interface SupervisorAssignmentStrategy {
    Professor assignSupervisor(TraineeshipPosition position, List<Professor> availableProfessors);
}
