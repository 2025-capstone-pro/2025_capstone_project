package com.samdaejjang.backend.repository;

import com.samdaejjang.backend.entity.VideoAnalysisWithFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoAnalysisRepository extends JpaRepository<VideoAnalysisWithFeedback, Long> {
}
