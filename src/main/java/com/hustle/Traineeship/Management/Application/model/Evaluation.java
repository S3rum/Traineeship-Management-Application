package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    // Evaluation by company
    private Integer motivationRating;
    private Integer effectivenessRating;
    private Integer efficiencyRating;

    // Evaluation by professor
    private Integer professorMotivationRating;
    private Integer professorEffectivenessRating;
    private Integer professorEfficiencyRating;
    private Integer companyFacilitiesRating;
    private Integer companyGuidanceRating;

    @OneToOne
    @JoinColumn(name = "position_id")
    private TraineeshipPosition traineeshipPosition;

    public void setTraineeshipPosition(TraineeshipPosition position) {
        this.traineeshipPosition = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TraineeshipPosition getTraineeshipPosition() {
        return traineeshipPosition;
    }

    public Integer getMotivationRating() {
        return motivationRating;
    }

    public void setMotivationRating(Integer motivationRating) {
        this.motivationRating = motivationRating;
    }

    public Integer getEffectivenessRating() {
        return effectivenessRating;
    }

    public void setEffectivenessRating(Integer effectivenessRating) {
        this.effectivenessRating = effectivenessRating;
    }

    public Integer getEfficiencyRating() {
        return efficiencyRating;
    }

    public void setEfficiencyRating(Integer efficiencyRating) {
        this.efficiencyRating = efficiencyRating;
    }

    public Integer getProfessorMotivationRating() {
        return professorMotivationRating;
    }

    public void setProfessorMotivationRating(Integer professorMotivationRating) {
        this.professorMotivationRating = professorMotivationRating;
    }

    public Integer getProfessorEffectivenessRating() {
        return professorEffectivenessRating;
    }

    public void setProfessorEffectivenessRating(Integer professorEffectivenessRating) {
        this.professorEffectivenessRating = professorEffectivenessRating;
    }

    public Integer getProfessorEfficiencyRating() {
        return professorEfficiencyRating;
    }

    public void setProfessorEfficiencyRating(Integer professorEfficiencyRating) {
        this.professorEfficiencyRating = professorEfficiencyRating;
    }

    public Integer getCompanyFacilitiesRating() {
        return companyFacilitiesRating;
    }

    public void setCompanyFacilitiesRating(Integer companyFacilitiesRating) {
        this.companyFacilitiesRating = companyFacilitiesRating;
    }

    public Integer getCompanyGuidanceRating() {
        return companyGuidanceRating;
    }

    public void setCompanyGuidanceRating(Integer companyGuidanceRating) {
        this.companyGuidanceRating = companyGuidanceRating;
    }
}
