package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.BodySpecRequest;
import com.samdaejjang.backend.dto.BodySpecResponse;
import com.samdaejjang.backend.entity.BodySpec;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.service.UserService;
import com.samdaejjang.backend.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/body-specs")
    public ResponseEntity<?> saveBodySpec(@RequestHeader("X-User-Id") String userId,
                                          @RequestBody BodySpecRequest bodySpecRequest) {

        Optional<Users> findUser = userService.findUser(Long.parseLong(userId));
        if (isEmpty(findUser)) {
            throw new RuntimeException("해당 요청의 사용자가 없습니다.");
        }
        BodySpec bodySpec = BodySpec.createBodySpec(findUser.get(), bodySpecRequest);

        BodySpec findBodySpec = userService.saveBodySpec(bodySpec);

        SuccessResponse<BodySpecResponse> response = new SuccessResponse<>(new BodySpecResponse(findBodySpec.getBodySpecId()));

        return ResponseEntity.ok(response);
    }

    private static boolean isEmpty(Optional<Users> findUser) {
        return !findUser.isPresent();
    }
}
