package com.samdaejjang.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "video_analysis_with_feedback")
public class VideoAnalysisWithFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false) // video_id 외래키 설정 (연관관계의 주인) , 단방향
    private ExerciseVideo exerciseVideo;

    @Column(name = "frame_time_sec", nullable = false)
    private float frameTimeSec;

    @Column(name = "is_anomaly", nullable = false)
    private boolean isAnomaly = true;

    @Column(name = "issue_description", nullable = false)
    private String issueDescription;

    @Column(name = "feedback_text", nullable = false, columnDefinition = "TEXT")
    private String feedbackText;

    @Column(name = "feedback_time", nullable = false)
    private LocalDateTime feedbackTime;
}
