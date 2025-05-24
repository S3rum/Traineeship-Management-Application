package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends User {

    private String companyName;

    private String location;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<TraineeshipPosition> traineeshipPositions;

    public Company() {
        super();
    }

    public Company(String username, String password, Role role, String companyName, String location) {
        super(username, password, role);
        this.companyName = companyName;
        this.location = location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
