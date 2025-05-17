package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.FrameDataRequest;
import com.samdaejjang.backend.service.FeedbackService;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("request")
    public Mono<?> startFeedback(@RequestBody FrameDataRequest request) {

        return feedbackService.generateFeedback(request)
        .<ResponseEntity<?>>map(feedback ->
            ResponseEntity.ok(new SuccessResponse<>(feedback)))
        .onErrorResume(e -> {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse));
        });
    }
}
