package com.samdaejjang.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnomalyResponse {
    private List<FrameFeedback> feedbackList;
}
