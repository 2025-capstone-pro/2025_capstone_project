package com.samdaejjang.backend.service;

import com.samdaejjang.backend.dto.FeedbackSaveRequestDto;
import com.samdaejjang.backend.entity.ExerciseVideo;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.entity.VideoAnalysisWithFeedback;
import com.samdaejjang.backend.repository.ExerciseVideoRepository;
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
    private final ExerciseVideoRepository exerciseVideoRepository;
    private final UsersRepository usersRepository;

    public ExerciseVideo save(FeedbackSaveRequestDto requestDTO) {

        Optional<Users> findUser = usersRepository.findById(requestDTO.getUserId());
        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        // 1. ExerciseVideo 생성
        ExerciseVideo video = new ExerciseVideo();
        video.setUser(findUser.get());
        video.setVideoUrl(requestDTO.getVideoUrl());

        ExerciseVideo savedVideo = exerciseVideoRepository.save(video);

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
}
