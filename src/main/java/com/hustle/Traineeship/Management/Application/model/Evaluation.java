package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    // Evaluation by company (e.g., for a traineeship in progress)
    private Integer motivationRating;
    private Integer effectivenessRating;
    private Integer efficiencyRating;

    // Evaluation by professor (for supervised positions)
    private Integer professorMotivationRating;
    private Integer professorEffectivenessRating;
    private Integer professorEfficiencyRating;
    private Integer companyFacilitiesRating;
    private Integer companyGuidanceRating;

    @OneToOne
    @JoinColumn(name = "position_id")
    private TraineeshipPosition traineeshipPosition;

    // Constructors, getters and setters
    // ...
}
