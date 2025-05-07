package com.samdaejjang.backend.entity;

import jakarta.persistence.*;

@Entity(name = "goals")
public class Goals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "goal_name", nullable = false)
    private String goalName;
}
