package com.samdaejjang.backend.repository;

import com.samdaejjang.backend.entity.ExerciseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<ExerciseVideo, Long> {

    @Query("SELECT v FROM exercise_videos v WHERE v.user.userId = :userId")
    List<ExerciseVideo> findAllByUserId(@Param("userId") Long userId);
}
