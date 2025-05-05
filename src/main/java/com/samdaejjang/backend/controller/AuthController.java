package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.config.JwtTokenProvider;
import com.samdaejjang.backend.dto.LoginRequestDto;
import com.samdaejjang.backend.dto.LoginResponseDto;
import com.samdaejjang.backend.dto.SignupRequestDto;
import com.samdaejjang.backend.dto.SignupResponseDto;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.UserRepository;
import com.samdaejjang.backend.service.UserService;
import com.samdaejjang.backend.utils.ApiResponse;
import com.samdaejjang.backend.utils.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {

        try {

            // Spring Security 인증 과정
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(), loginRequestDto.getPassword()
                    )
            );

            log.info("authenticated user: {}", authentication.getName());

            Users user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwt = jwtTokenProvider.createToken(authentication.getName());

            LoginResponseDto loginResponseDto = new LoginResponseDto(jwt, user.getUserId(), user.getUsername(), user.getNickname());
            return ResponseEntity.ok(new ApiResponse<>(true, loginResponseDto));
        } catch (Exception e) {
            ApiResponse<ErrorResponse> response = new ApiResponse<>(false, new ErrorResponse(e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }

    }
}
