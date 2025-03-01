package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "traineeship_logbook")
public class TraineeshipLogBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    @Lob  // Use @Lob if the description can be long.
    private String description;

    // Many log entries can belong to one student.
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // --- Constructors ---
    public TraineeshipLogBook() { }

    public TraineeshipLogBook(LocalDate startDate, LocalDate endDate, String description, Student student) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.student = student;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
}
