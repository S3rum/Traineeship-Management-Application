package com.hustle.Traineeship.Management.Application.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "traineeship_positions")
public class TraineeshipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    private List<String> requiredSkills;

    @ElementCollection
    private List<String> topics;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor supervisor;

    @OneToOne(mappedBy = "traineeshipPosition", cascade = CascadeType.ALL)
    private Evaluation evaluation;

    public List<String> getTopics() {
        return topics;
    }

    // Constructors, getters and setters
    // ...
}
