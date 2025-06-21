package com.samdaejjang.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "video_analysis_with_feedback")
public class VideoAnalysisWithFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false) // video_id 외래키 설정 (연관관계의 주인) , 단방향
    private ExerciseVideo exerciseVideo;

    @Column(name = "frame", nullable = false)
    private int frame;

    @Column(name = "frame_timestamp", nullable = false)
    private float frameTimestamp;

    @Column(name = "feedback_text", nullable = false, columnDefinition = "TEXT")
    private String feedbackText;
}
