package com.samdaejjang.backend.client;

import com.samdaejjang.backend.dto.AnomalyResponse;
import com.samdaejjang.backend.dto.FrameDataRequest;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AnomalyClient {

    @Value("${anomaly.api.url}")
    private String anomalyApiUrl;

    private final WebClient webClient = WebClient.create();

    public Mono<AnomalyResponse> getAnomalies(FrameDataRequest request) {
        return webClient.post()
                .uri(anomalyApiUrl + "/detect-anomaly")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AnomalyResponse.class);
    }
}