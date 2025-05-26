package com.samdaejjang.backend.service;

import com.samdaejjang.backend.client.AnomalyClient;
import com.samdaejjang.backend.client.LLMClient;
import com.samdaejjang.backend.dto.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final AnomalyClient anomalyClient;
    private final LLMClient llmClient;

    public Mono<FeedbackResponse> generateFeedback(FrameDataRequest request) {
        // 순차적 비동기 처리: 이상치 탐지 후 LLM 피드백 요청
        return anomalyClient.getAnomalies(request)
                .flatMap(anomalyResult -> {
                    PoseRequestDto poseRequestDto = new PoseRequestDto(
                            request.getFrames(),
                            anomalyResult.getAnomalies()
                    );
                    return llmClient.getFeedback(poseRequestDto);
                });
    }

    public Mono<RoutineResponseDto> generateRoutine(String promptText) {

        RoutineRequestDto dto = new RoutineRequestDto();
        dto.setPromptText(promptText);

        return llmClient.getRoutine(dto);
    }
}
