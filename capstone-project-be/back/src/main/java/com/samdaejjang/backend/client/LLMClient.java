package com.samdaejjang.backend.client;

import com.samdaejjang.backend.dto.FeedbackResponse;
import com.samdaejjang.backend.dto.PoseRequestDto;
import com.samdaejjang.backend.dto.RoutineRequestDto;
import com.samdaejjang.backend.dto.RoutineResponseDto;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class LLMClient {

    @Value("${llm.api.url}")
    private String llmApiUrl;

    private final WebClient webClient = WebClient.create();

    public Mono<FeedbackResponse> getFeedback(PoseRequestDto requestDto) {
        return webClient
                .post()
                .uri(llmApiUrl + "/feedback")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(FeedbackResponse.class);
    }

    public Mono<RoutineResponseDto> getRoutine(RoutineRequestDto requestDto) {
        return webClient
                .post()
                .uri(llmApiUrl + "/routine")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(RoutineResponseDto.class);
    }
}
