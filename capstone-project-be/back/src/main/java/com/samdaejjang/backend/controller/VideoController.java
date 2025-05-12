package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.BodySpecResponse;
import com.samdaejjang.backend.dto.ExerciseVideoRequestDTO;
import com.samdaejjang.backend.entity.ExerciseVideo;
import com.samdaejjang.backend.service.ExerciseVideoService;
import com.samdaejjang.backend.service.S3Service;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {

    private final ExerciseVideoService exerciseVideoService;
    private final S3Service s3Service;

    /**
     * 프론트로부터 Presigned URL 요청받는 엔드포인트
     */
    @GetMapping("/presigned-url")
    public ResponseEntity<String> presignedUrl(@RequestParam String fileName, @RequestParam String contentType) {

        String url = s3Service.generatePresignedUrl(fileName, contentType);
        return ResponseEntity.ok(url);
    }

    /**
     * 운동 영상 메타데이터 DB에 저장하는 요청
     */
    @PostMapping("/metadata")
    public ResponseEntity<?> saveExerciseVideoMetadata(@RequestBody ExerciseVideoRequestDTO requestDTO) {

        try {
            ExerciseVideo savedVideo = exerciseVideoService.save(requestDTO);
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

}
