package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExerciseRecordRequestDto {
    private Long userId;
    private String exerciseName;
    private Integer setCount;
    private Float weightKg;
    private Integer durationMin;
    private String subjectiveFeedback;
    private LocalDateTime performedAt;
}
