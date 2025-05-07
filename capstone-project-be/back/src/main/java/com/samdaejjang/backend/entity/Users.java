package com.samdaejjang.backend.entity;

import com.samdaejjang.backend.dto.KakaoUserInfo;
import com.samdaejjang.backend.dto.SignupRequestDto;
import com.samdaejjang.backend.utils.AuthProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username; // 아이디 (이메일)

    private String password;

    private String nickname;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider; // LOCAL, KAKAO 등

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // UserGoals 와 양방향 관계를 위한 설정
    private List<UserGoals> userGoals;

    public static Users createKakaoUser(KakaoUserInfo info) {
        return Users.builder()
                .username(info.getKakao_account().getEmail())
                .nickname(info.getKakao_account().getProfile().getNickname())
                .phoneNumber(info.getKakao_account().getPhone_number())
                .provider(AuthProvider.KAKAO)
                .build();
    }

    public static Users createUser(SignupRequestDto requestDto) {
        return Users.builder()
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .nickname(requestDto.getNickname())
                .phoneNumber(requestDto.getPhoneNumber())
                .provider(AuthProvider.LOCAL)
                .build();
    }


}