package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.AuthResponse;
import com.samdaejjang.backend.dto.SignupRequestDto;
import com.samdaejjang.backend.dto.SignupResponseDto;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.service.UserService;
import com.samdaejjang.backend.utils.ApiResponse;
import com.samdaejjang.backend.utils.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {

        try {
            Users registered = userService.register(signupRequestDto);

            if (registered.getUserId() > 0) {

                SignupResponseDto signupResponseDto = new SignupResponseDto(
                        registered.getUserId(),
                        registered.getUsername(),
                        registered.getNickname(),
                        registered.getPhoneNumber()
                );

                ApiResponse<SignupResponseDto> response = new ApiResponse<>(true, signupResponseDto);

                return ResponseEntity.ok(response);
            } else {
                ErrorResponse error = new ErrorResponse("회원 등록 실패");
                ApiResponse<ErrorResponse> response = new ApiResponse<>(false, error);
                return ResponseEntity.badRequest().body(response);
            }


        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(e.getMessage());
            ApiResponse<ErrorResponse> response = new ApiResponse<>(false, error);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
