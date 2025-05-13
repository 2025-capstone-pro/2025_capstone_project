package com.samdaejjang.backend.repository;

import com.samdaejjang.backend.entity.ExerciseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {

    List<ExerciseRecord> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end);
}
