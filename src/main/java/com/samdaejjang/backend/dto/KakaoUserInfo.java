package com.samdaejjang.backend.dto;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        private String email;
        private String phone_number;
        private Profile profile;

        @Data
        public static class Profile {
            private String nickname;
        }
    }
}
