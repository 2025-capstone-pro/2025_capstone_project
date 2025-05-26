package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.*;
import com.samdaejjang.backend.entity.ExerciseVideo;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.UsersRepository;
import com.samdaejjang.backend.repository.VideoRepository;
import com.samdaejjang.backend.service.VideoService;
import com.samdaejjang.backend.service.LLMService;
import com.samdaejjang.backend.service.S3Service;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {

    private final VideoService videoService;
    private final LLMService LLMService;
    private final S3Service s3Service;
    private final UsersRepository usersRepository;
    private final VideoRepository videoRepository;

    /**
     * 운동 영상 피드백 요청받는 엔드포인트
     */
    @PostMapping("feedback")
    public Mono<?> startFeedback(@RequestBody FrameDataRequest request) {

        return LLMService.generateFeedback(request)
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
    public ResponseEntity<?> presignedUrl(@RequestHeader("X-User-Id") String userId,
                                          @RequestParam String fileName) {

        try {
            Optional<Users> findUser = usersRepository.findById(Long.parseLong(userId));
            if (!findUser.isPresent()) {
                throw new EntityNotFoundException("찾는 회원 없음");
            }

            String s3Key = "upload/" + fileName;

            String presignedUrl = s3Service.generatePresignedUrl(s3Key);

            //DB에 정보 저장
            ExerciseVideo exerciseVideo = new ExerciseVideo();
            exerciseVideo.setVideoName(fileName);
            exerciseVideo.setS3Key(s3Key);
            exerciseVideo.setUser(findUser.get());

            ExerciseVideo saved = videoRepository.save(exerciseVideo);

            Map<String, ?> response = Map.of("presignedUrl", presignedUrl, "videoId", saved.getVideoId());

            return ResponseEntity.ok(new SuccessResponse<>(response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * 피드백 정보를 DB에 저장하는 요청
     */
    @PostMapping("/metadata")
    public ResponseEntity<?> saveVideoMetadataAndFeedback(@RequestHeader("X-User-Id") String userId,
                                                          @RequestBody FeedbackSaveRequestDto requestDTO) {

        try {
            videoService.save(Long.parseLong(userId), requestDTO);

            SuccessResponse<ExerciseVideoResponseDTO> response = new SuccessResponse<>(new ExerciseVideoResponseDTO("성공적으로 저장됨"));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Data
    @AllArgsConstructor
    public static class ExerciseVideoResponseDTO {
        private String msg;
    }

    /**
     * 해당 사용자의 분석된 운동 영상 리스트를 조회하는 엔드포인트
     */
    @GetMapping
    public ResponseEntity<?> getUserVideos(@RequestHeader("X-User-Id") String userId) {

        try {
            List<VideoSummaryDto> result = videoService.getVideosList(Long.parseLong(userId));
            return ResponseEntity.ok(new SuccessResponse<>(result));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * 특정 영상에 대한 피드백 정보를 요청받는 엔드포인트
     */
    @GetMapping("/{videoId}")
    public ResponseEntity<?> getVideoDetail(@PathVariable("videoId") Long videoId) {

        try {
            VideoFeedbackDetailDto response = videoService.getVideoDetails(videoId);
            return ResponseEntity.ok(new SuccessResponse<>(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
