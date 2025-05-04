package com.samdaejjang.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "body_spec")
public class BodySpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_spec_id")
    private Long bodySpecId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "height_cm", nullable = false)
    private float heightCm;

    @Column(name = "weight_kg", nullable = false)
    private float weightKg;

    @Column(name = "skeletal_muscle_mass", nullable = false)
    private float skeletalMuscleMass;

    @Column(name = "fat_mass", nullable = false)
    private float fatMass;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
