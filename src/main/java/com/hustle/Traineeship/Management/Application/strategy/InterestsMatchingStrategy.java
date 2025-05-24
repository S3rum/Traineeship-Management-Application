package com.hustle.Traineeship.Management.Application.strategy;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import java.util.List;
import java.util.stream.Collectors;

public class InterestsMatchingStrategy implements PositionAssignmentStrategy {

    private double threshold;

    public InterestsMatchingStrategy(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public List<TraineeshipPosition> filterPositions(Student student, List<TraineeshipPosition> availablePositions) {
        List<String> studentInterests = student.getInterests();
        return availablePositions.stream().filter(position -> {
            List<String> positionTopics = position.getTopics();
            double similarity = computeJacquardSimilarity(studentInterests, positionTopics);
            return similarity > threshold;
        }).collect(Collectors.toList());
    }

    private double computeJacquardSimilarity(List<String> set1, List<String> set2) {
        if (set1 == null || set2 == null || (set1.isEmpty() && set2.isEmpty())) {
            return 0;
        }
        long intersection = set1.stream().filter(set2::contains).count();
        long union = set1.size() + set2.size() - intersection;
        return union == 0 ? 0 : (double) intersection / union;
    }
}
