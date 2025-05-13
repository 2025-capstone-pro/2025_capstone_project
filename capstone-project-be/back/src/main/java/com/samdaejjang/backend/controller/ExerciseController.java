package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.ExerciseRecordRequestDto;
import com.samdaejjang.backend.dto.ExerciseRecordResponseDto;
import com.samdaejjang.backend.service.ExerciseRecordService;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseRecordService exerciseRecordService;

    /**
     * 운동 기록 저장 요청 받는 엔드포인트
     */
    @PostMapping
    public ResponseEntity<?> createExerciseRecord(@RequestBody ExerciseRecordRequestDto requestDto) {

        try {
            Long exerciseRecordId = exerciseRecordService.saveExerciseRecord(requestDto);

            Map<String, Long> map = new HashMap<>();
            map.put("recordId", exerciseRecordId);
            SuccessResponse<Map<String, Long>> response = new SuccessResponse<>(map);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<?> getRecordsByDate(@RequestParam("userId") Long userid,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        try {
            List<ExerciseRecordResponseDto> list = exerciseRecordService.getRecordByDate(userid, date);

            SuccessResponse<List<ExerciseRecordResponseDto>> response = new SuccessResponse<>(list);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    


}
