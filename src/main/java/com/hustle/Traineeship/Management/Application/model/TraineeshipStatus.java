package com.hustle.Traineeship.Management.Application.model;

public enum TraineeshipStatus {
    PENDING, // Traineeship created, not yet started or student not assigned
    IN_PROGRESS, // Actively ongoing
    AWAITING_EVALUATION, // Finished, waiting for all evaluations
    EVALUATION_COMPLETED, // All evaluations are in
    COMPLETED_PASS, // Finalized by committee as PASS
    COMPLETED_FAIL, // Finalized by committee as FAIL
    CANCELLED // Cancelled before completion
}