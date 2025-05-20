package com.samdaejjang.backend.service;

import com.samdaejjang.backend.dto.FeedbackSaveRequestDto;
import com.samdaejjang.backend.dto.VideoSummaryDto;
import com.samdaejjang.backend.entity.ExerciseVideo;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.entity.VideoAnalysisWithFeedback;
import com.samdaejjang.backend.repository.VideoRepository;
import com.samdaejjang.backend.repository.UsersRepository;
import com.samdaejjang.backend.repository.VideoAnalysisRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoAnalysisRepository analysisRepository;
    private final VideoRepository videoRepository;
    private final UsersRepository usersRepository;

    public ExerciseVideo save(FeedbackSaveRequestDto requestDTO) {

        Optional<Users> findUser = usersRepository.findById(requestDTO.getUserId());
        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("해당 요청의 사용자가 없음");
        }

        // 1. ExerciseVideo 생성
        ExerciseVideo video = new ExerciseVideo();
        video.setUser(findUser.get());
        video.setVideoUrl(requestDTO.getVideoUrl());

        ExerciseVideo savedVideo = videoRepository.save(video);

        // 2. 프레임 피드백 → VideoAnalysisWithFeedback 변환
        List<VideoAnalysisWithFeedback> feedbackEntities = requestDTO.getFeedbackList().stream()
                .map(frame -> {
                    VideoAnalysisWithFeedback analysis = new VideoAnalysisWithFeedback();
                    analysis.setExerciseVideo(savedVideo);

                    analysis.setFrame(frame.getFrame());
                    analysis.setIssueDescription("자동 감지된 이상 동작"); // 향후 LLM 분석으로 대체 가능
                    analysis.setFeedbackText(frame.getText());
                    analysis.setFeedbackTime(LocalDateTime.now());
                    return analysis;
                })
                .toList();

        // 3. 저장
        analysisRepository.saveAll(feedbackEntities);

        return savedVideo;
    }

    public List<VideoSummaryDto> getVideosList(Long userId) {

        Optional<Users> findUser = usersRepository.findById(userId);

        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("해당 요청의 사용자가 없음");
        }

        List<ExerciseVideo> videosList = videoRepository.findAllByUserId(userId);

        if (videosList.isEmpty()) {
            throw new RuntimeException("저장된 영상이 없음");
        }

        return videosList.stream()
                .map(video -> new VideoSummaryDto(
                        video.getVideoId(),
                        video.getVideoUrl(),
                        video.getRecordedAt()
                ))
                .toList();
    }
}
