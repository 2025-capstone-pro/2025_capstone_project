package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.service.RoutineService;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/request")
    public Mono<?> recommand(@RequestBody String promptText) {

        return routineService.generateRoutine(promptText)
                .<ResponseEntity<?>>map(routine ->
                        ResponseEntity.ok(new SuccessResponse<>(routine)))
                .onErrorResume(e -> {
                    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(errorResponse));
                });
    }
}