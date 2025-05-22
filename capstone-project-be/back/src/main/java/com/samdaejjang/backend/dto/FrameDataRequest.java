package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrameDataRequest {
    private List<FrameDto> frames;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class FrameDto {
        private Long frameIndex;
        private Long timestamp;
        private List<LandmarkDto> landmarks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class LandmarkDto {
        private float x;
        private float y;
        private float z;
    }
}