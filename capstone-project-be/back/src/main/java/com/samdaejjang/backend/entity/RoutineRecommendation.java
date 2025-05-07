package com.samdaejjang.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "routine_recommendations")
public class RoutineRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래키 (연관관계의 주인)
    private Users user;

    @Column(name = "routine_recommendation", nullable = false, columnDefinition = "TEXT")
    private String routineRecommendation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 추천 생성 일시
}
