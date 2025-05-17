package com.samdaejjang.backend.service;

import com.samdaejjang.backend.client.AnomalyClient;
import com.samdaejjang.backend.client.LLMClient;
import com.samdaejjang.backend.dto.FeedbackResponse;
import com.samdaejjang.backend.dto.FrameDataRequest;
import com.samdaejjang.backend.dto.LLMRequest;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final AnomalyClient anomalyClient;
    private final LLMClient llmClient;

    public Mono<FeedbackResponse> generateFeedback(FrameDataRequest request) {
        // 순차적 비동기 처리: 이상치 탐지 후 LLM 피드백 요청
        return anomalyClient.getAnomalies(request)
                .flatMap(anomalyResult -> {
                    LLMRequest llmRequest = new LLMRequest(
                            request.getFrames(),
                            anomalyResult.getAnomalies()
                    );
                    return llmClient.getFeedback(llmRequest);
                });
    }
}
