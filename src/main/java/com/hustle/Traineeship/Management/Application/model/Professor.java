package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professors")
public class Professor extends User {

    private String fullName;

    @ElementCollection
    private List<String> interests;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL)
    private List<TraineeshipPosition> supervisedPositions;

    public Professor() {
        super();
    }

    public Professor(String username, String password, Role role, String fullName, List<String> interests) {
        super(username, password, role);
        this.fullName = fullName;
        this.interests = interests;
    }

    // Getter and Setter for fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for interests
    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    // Optionally, getters and setters for supervisedPositions if needed
}
