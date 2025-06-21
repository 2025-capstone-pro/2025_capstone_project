package com.samdaejjang.backend.service;

import com.samdaejjang.backend.client.RoutineClient;
import com.samdaejjang.backend.dto.RoutineRequestDto;
import com.samdaejjang.backend.dto.RoutineResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineClient routineClient;

    public Mono<RoutineResponseDto> generateRoutine(String promptText) {

        RoutineRequestDto dto = new RoutineRequestDto();
        dto.setPromptText(promptText);

        return routineClient.getRoutine(dto);
    }
}
