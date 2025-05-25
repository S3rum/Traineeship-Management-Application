package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "committees")
public class Committee extends User {
    public Committee() {
        super();
    }
    public Committee(String username, String password, Role role) {
        super(username, password, role);
    }
}