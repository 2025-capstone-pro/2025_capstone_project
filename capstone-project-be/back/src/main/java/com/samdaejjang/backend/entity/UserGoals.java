package com.samdaejjang.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "user_goals")
public class UserGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_goal_id")
    private Long userGoalId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 이 테이블에 user_id 라는 외래키 생성 (연관관계의 주인)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false) // 이 테이블에 goal_id 라는 외래키 생성 (연관관계의 주인)
    private Goals goal;

    @Column(name = "set_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime setAt;
}
