package com.hustle.Traineeship.Management.Application.strategy;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;

public interface PositionAssignmentStrategy {
    List<TraineeshipPosition> filterPositions(Student student, List<TraineeshipPosition> availablePositions);
}
