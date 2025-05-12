package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExerciseVideoRequestDTO {

    private Long userId;
    private String videoUrl;
}
