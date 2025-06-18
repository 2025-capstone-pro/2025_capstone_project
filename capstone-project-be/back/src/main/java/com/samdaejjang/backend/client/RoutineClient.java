package com.samdaejjang.backend.client;

import com.samdaejjang.backend.dto.RoutineRequestDto;
import com.samdaejjang.backend.dto.RoutineResponseDto;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class RoutineClient {

    @Value("${llm.api.url}")
    private String llmApiUrl;

    private final WebClient webClient = WebClient.create();

    public Mono<RoutineResponseDto> getRoutine(RoutineRequestDto requestDto) {
        return webClient
                .post()
                .uri(llmApiUrl + "/ask")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(RoutineResponseDto.class);
    }
}
