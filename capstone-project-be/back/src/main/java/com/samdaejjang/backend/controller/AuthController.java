package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.config.JwtTokenProvider;
import com.samdaejjang.backend.dto.LoginRequestDto;
import com.samdaejjang.backend.dto.LoginResponseDto;
import com.samdaejjang.backend.dto.SignupRequestDto;
import com.samdaejjang.backend.dto.SignupResponseDto;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.UsersRepository;
import com.samdaejjang.backend.service.UserService;
import com.samdaejjang.backend.utils.ErrorResponse;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {

        try {
            Users registered = userService.register(signupRequestDto);

            SignupResponseDto signupResponseDto = new SignupResponseDto(
                    registered.getUserId(),
                    registered.getUsername(),
                    registered.getNickname(),
                    registered.getPhoneNumber()
            );

            SuccessResponse<SignupResponseDto> response = new SuccessResponse<>(signupResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
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

            Users user = usersRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwt = jwtTokenProvider.createToken(authentication.getName());

            SuccessResponse<LoginResponseDto> response = new SuccessResponse<>(new LoginResponseDto(jwt, user.getUserId(), user.getUsername(), user.getNickname()));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("이메일 또는 비밀번호가 일치하지 않습니다."));
        }

    }
}
