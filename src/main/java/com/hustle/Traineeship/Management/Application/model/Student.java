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

    // Constructors, getters and setters

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

    public List<String> getInterests() {
        return interests;
    }

    // Getters and setters for all fields
    // ...
}
