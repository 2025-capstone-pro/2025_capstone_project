package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameFeedbackDetailDto {
    private int frame;
    private float timeSec;
    private String feedbackText;
}
