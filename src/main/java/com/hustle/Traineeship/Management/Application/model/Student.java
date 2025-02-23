package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends User {

    private String fullName;

    @Column(unique = true)
    private String universityId;

    @ElementCollection
    private List<String> interests;

    @ElementCollection
    private List<String> skills;

    private String preferredLocation;

    // One-to-one relationship with a traineeship position (if assigned)
    @OneToOne(mappedBy = "student")
    private TraineeshipPosition traineeshipPosition;

    // Constructors

    public Student() {
        super();
    }

    public Student(String username, String password, Role role, String fullName,
                   String universityId, List<String> interests, List<String> skills,
                   String preferredLocation) {
        super(username, password, role);
        this.fullName = fullName;
        this.universityId = universityId;
        this.interests = interests;
        this.skills = skills;
        this.preferredLocation = preferredLocation;
    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public TraineeshipPosition getTraineeshipPosition() {
        return traineeshipPosition;
    }

    public void setTraineeshipPosition(TraineeshipPosition traineeshipPosition) {
        this.traineeshipPosition = traineeshipPosition;
    }
}
