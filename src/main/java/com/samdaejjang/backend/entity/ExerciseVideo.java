package com.samdaejjang.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity(name = "exercise_videos")
public class ExerciseVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long videoId;

    @ManyToOne // Users 와는 단방향 관계 (이 객체만 Users 를 참조함)
    @JoinColumn(name = "user_id", nullable = false) // user_id 외래키 설정 (연관관계의 주인)
    private Users user;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime recordedAt;
}
