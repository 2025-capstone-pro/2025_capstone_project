package com.samdaejjang.backend.dto;

import com.samdaejjang.backend.entity.ExerciseRecord;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExerciseRecordResponseDto {

    private Long recordId;
    private String exerciseName;
    private Integer setCount;
    private Float weightKg;
    private Integer durationMin;
    private String subjectiveFeedback;
    private LocalDateTime performedAt;

    public static ExerciseRecordResponseDto fromEntity(ExerciseRecord entity) {
        ExerciseRecordResponseDto dto = new ExerciseRecordResponseDto();
        dto.setRecordId(entity.getRecordId());
        dto.setExerciseName(entity.getExerciseName());
        dto.setSetCount(entity.getSetCount());
        dto.setWeightKg(entity.getWeightKg());
        dto.setDurationMin(entity.getDurationMin());
        dto.setSubjectiveFeedback(entity.getSubjectiveFeedback());
        dto.setPerformedAt(entity.getPerformedAt());
        return dto;
    }
}
