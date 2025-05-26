package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoFeedbackDetailDto {
    private Long videoId;
    private String s3Key;
    private LocalDateTime analyzedAt;
    private List<FrameFeedbackDetailDto> feedbackList;
}