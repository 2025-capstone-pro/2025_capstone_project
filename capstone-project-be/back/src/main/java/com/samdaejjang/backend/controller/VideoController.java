package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.BodySpecResponse;
import com.samdaejjang.backend.dto.FeedbackSaveRequestDto;
import com.samdaejjang.backend.dto.FrameDataRequest;
import com.samdaejjang.backend.dto.VideoSummaryDto;
import com.samdaejjang.backend.entity.ExerciseVideo;
import com.samdaejjang.backend.service.VideoService;
import com.samdaejjang.backend.service.FeedbackService;
import com.samdaejjang.backend.service.S3Service;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {

    private final VideoService videoService;
    private final S3Service s3Service;
    private final FeedbackService feedbackService;

    /**
     * 운동 영상 피드백 요청받는 엔드포인트
     */
    @PostMapping("feedback")
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

    /**
     * 프론트로부터 Presigned URL 요청받는 엔드포인트
     */
    @GetMapping("/presigned-url")
    public ResponseEntity<String> presignedUrl(@RequestParam String fileName, @RequestParam String contentType) {

        String url = s3Service.generatePresignedUrl(fileName, contentType);
        return ResponseEntity.ok(url);
    }

    /**
     * 운동 영상 메타데이터, 피드백 정보를 DB에 저장하는 요청
     */
    @PostMapping("/metadata")
    public ResponseEntity<?> saveVideoMetadataAndFeedback(@RequestBody FeedbackSaveRequestDto requestDTO) {

        try {
            ExerciseVideo savedVideo = videoService.save(requestDTO);

            SuccessResponse<ExerciseVideoResponseDTO> response = new SuccessResponse<>(new ExerciseVideoResponseDTO(
                    savedVideo.getVideoId(),
                    savedVideo.getUser().getUserId()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Data
    @AllArgsConstructor
    public static class ExerciseVideoResponseDTO {
        private Long videoId;
        private Long userId;
    }

    /**
     * 해당 사용자의 분석된 운동 영상 리스트를 조회하는 엔드포인트
     */
    @GetMapping
    public ResponseEntity<?> getUserVideos(@RequestParam("userId") Long userId) {

        try {
            List<VideoSummaryDto> result = videoService.getVideosList(userId);
            return ResponseEntity.ok(new SuccessResponse<>(result));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
