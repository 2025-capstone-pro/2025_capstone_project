package com.samdaejjang.backend.service;

import com.samdaejjang.backend.dto.ExerciseVideoRequestDTO;
import com.samdaejjang.backend.entity.ExerciseVideo;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.ExerciseVideoRepository;
import com.samdaejjang.backend.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseVideoService {

    private final ExerciseVideoRepository exerciseVideoRepository;
    private final UsersRepository usersRepository;

    public ExerciseVideo save(ExerciseVideoRequestDTO requestDTO) {

        Optional<Users> findUser = usersRepository.findById(requestDTO.getUserId());
        if (!findUser.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        ExerciseVideo video = new ExerciseVideo();
        video.setUser(findUser.get());
        video.setVideoUrl(requestDTO.getVideoUrl());

        return exerciseVideoRepository.save(video);
    }
}
