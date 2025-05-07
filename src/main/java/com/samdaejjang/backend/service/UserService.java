package com.samdaejjang.backend.service;

import com.samdaejjang.backend.dto.SignupRequestDto;
import com.samdaejjang.backend.entity.BodySpec;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.BodySpecRepository;
import com.samdaejjang.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BodySpecRepository bodySpecRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 등록 메서드
    public Users register(SignupRequestDto dto) {

        // DB 중복 확인
        Optional<Users> findUser = userRepository.findByUsername(dto.getUsername());
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 해싱
        String hashPwd = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hashPwd);

        Users buildUser = Users.createUser(dto);

        Users savedUser = userRepository.save(buildUser);

        return savedUser;
    }


    public Optional<Users> findUser(Long userId) {
        return userRepository.findById(userId);
    }

    public BodySpec saveBodySpec(BodySpec bodySpec) {
        return bodySpecRepository.save(bodySpec);
    }
}
