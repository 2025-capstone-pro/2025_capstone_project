package com.samdaejjang.backend.entity;

import com.samdaejjang.backend.dto.ExerciseRecordRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity(name = "exercise_record")
public class ExerciseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 외래키 (연관 관계의 주인), 단방향
    private Users user;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Column(name = "set_count")
    private Integer setCount;

    @Column(name = "weight_kg")
    private Float weightKg;

    @Column(name = "duration_min")
    private Integer durationMin;

    @Column(name = "subjective_fedback", columnDefinition = "TEXT")
    private String subjectiveFeedback;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    public static ExerciseRecord fromDto(ExerciseRecordRequestDto dto, Users user) {
        ExerciseRecord record = new ExerciseRecord();
        record.user = user;
        record.exerciseName = dto.getExerciseName();
        record.setCount = dto.getSetCount();
        record.weightKg = dto.getWeightKg();
        record.durationMin = dto.getDurationMin();
        record.subjectiveFeedback = dto.getSubjectiveFeedback();
        record.performedAt = dto.getPerformedAt();
        return record;
    }
}
