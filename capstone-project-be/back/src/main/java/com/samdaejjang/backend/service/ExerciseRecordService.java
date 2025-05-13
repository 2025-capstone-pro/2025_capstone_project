package com.samdaejjang.backend.service;

import com.samdaejjang.backend.dto.ExerciseRecordRequestDto;
import com.samdaejjang.backend.dto.ExerciseRecordResponseDto;
import com.samdaejjang.backend.entity.ExerciseRecord;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.ExerciseRecordRepository;
import com.samdaejjang.backend.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseRecordService {

    private final ExerciseRecordRepository exerciseRecordRepository;
    private final UsersRepository usersRepository;

    public Long saveExerciseRecord(ExerciseRecordRequestDto requestDto) {
        Optional<Users> findUser = usersRepository.findById(requestDto.getUserId());
        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("해당 유저 정보 없음");
        }

        ExerciseRecord record = ExerciseRecord.fromDto(requestDto, findUser.get());

        ExerciseRecord save = exerciseRecordRepository.save(record);
        return save.getRecordId();
    }

    public List<ExerciseRecordResponseDto> getRecordByDate(Long userId, LocalDate date) {

        Optional<Users> findUser = usersRepository.findById(userId);
        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("해당 유저 정보 없음");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<ExerciseRecord> records = exerciseRecordRepository.findByPerformedAtBetween(start, end);
        if (records.isEmpty()) {
            throw new RuntimeException("운동 기록 없음");
        }

        ArrayList<ExerciseRecordResponseDto> result = new ArrayList<>();
        for (ExerciseRecord record : records) {
            result.add(ExerciseRecordResponseDto.fromEntity(record));
        }

        return result;
    }
}
