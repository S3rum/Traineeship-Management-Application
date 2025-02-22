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

    // Getters and setters
    // ...
}
