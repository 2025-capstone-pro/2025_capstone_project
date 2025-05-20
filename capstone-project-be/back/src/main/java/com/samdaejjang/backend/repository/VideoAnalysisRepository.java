package com.samdaejjang.backend.repository;

import com.samdaejjang.backend.entity.VideoAnalysisWithFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoAnalysisRepository extends JpaRepository<VideoAnalysisWithFeedback, Long> {

    @Query("SELECT v FROM video_analysis_with_feedback v WHERE v.exerciseVideo.videoId = :videoId")
    List<VideoAnalysisWithFeedback> findAllByVideoId(Long videoId);
}
