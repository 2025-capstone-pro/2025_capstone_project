package com.samdaejjang.backend.service;

import com.samdaejjang.backend.client.AnomalyClient;
import com.samdaejjang.backend.dto.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnomalyService {

    private final AnomalyClient anomalyClient;

    public Mono<AnomalyResponse> generateFeedback(FrameDataRequest request) {

        return anomalyClient.getAnomalies(request);
    }
}
