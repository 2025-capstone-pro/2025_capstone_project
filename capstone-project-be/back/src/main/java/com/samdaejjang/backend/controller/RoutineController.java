package com.samdaejjang.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {

    @GetMapping("/recommand")
    public String recommand() {
        return "recommand";
    }
}

// 운동 목적(DB 저장되어 있는 것),
