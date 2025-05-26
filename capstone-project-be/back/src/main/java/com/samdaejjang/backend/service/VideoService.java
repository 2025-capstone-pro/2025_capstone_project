package com.samdaejjang.backend.service;

import com.samdaejjang.backend.dto.FeedbackSaveRequestDto;
import com.samdaejjang.backend.dto.FrameFeedbackDetailDto;
import com.samdaejjang.backend.dto.VideoFeedbackDetailDto;
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

    public void save(Long userId, FeedbackSaveRequestDto requestDTO) {

        Optional<Users> findUser = usersRepository.findById(userId);
        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("해당 요청의 사용자가 없음");
        }

        Optional<ExerciseVideo> findVideo = videoRepository.findById(requestDTO.getVideoId());
        // 2. 프레임 피드백 → VideoAnalysisWithFeedback 변환
        List<VideoAnalysisWithFeedback> feedbackEntities = requestDTO.getFeedbackList().stream()
                .map(frame -> {
                    VideoAnalysisWithFeedback analysis = new VideoAnalysisWithFeedback();
                    analysis.setExerciseVideo(findVideo.get());

                    analysis.setFrame(frame.getFrame());
                    analysis.setFrameTimestamp(frame.getTimestamp());
                    analysis.setFeedbackText(frame.getText());
                    return analysis;
                })
                .toList();

        // 3. 저장
        analysisRepository.saveAll(feedbackEntities);
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
                        video.getS3Key(),
                        video.getRecordedAt()
                ))
                .toList();
    }

    public VideoFeedbackDetailDto getVideoDetails(Long videoId) {

        ExerciseVideo video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("해당 영상이 존재하지 않음."));

        List<VideoAnalysisWithFeedback> analysisList = analysisRepository.findAllByVideoId(videoId);

        if (analysisList.isEmpty()) {
            throw new RuntimeException("해당 영상과 관련한 피드백이 없음");
        }

        List<FrameFeedbackDetailDto> feedbacks = analysisList.stream()
                .map(a -> new FrameFeedbackDetailDto(
                        a.getFrame(), // assuming 30fps
                        a.getFrameTimestamp(),
                        a.getFeedbackText()
                ))
                .toList();

        return new VideoFeedbackDetailDto(
                video.getVideoId(),
                video.getS3Key(),
                video.getRecordedAt(),
                feedbacks
        );
    }
}
