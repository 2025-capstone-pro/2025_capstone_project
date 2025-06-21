package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VideoSummaryDto {

    private Long videoId;
    private String s3Key;
    private LocalDateTime createdAt;
}
