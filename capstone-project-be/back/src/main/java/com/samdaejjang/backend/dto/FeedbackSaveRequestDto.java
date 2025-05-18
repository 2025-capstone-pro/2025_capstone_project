package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackSaveRequestDto {
    private Long userId;
    private String videoUrl;
    private List<FrameFeedbackDto> feedbackList;
}