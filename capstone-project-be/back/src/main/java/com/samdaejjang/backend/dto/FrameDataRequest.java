package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrameDataRequest {
    private String userId;
    private List<Frame> frames;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Frame {
        private Long frameId;
        private Long timestamp;
        private List<PoseData> poseData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PoseData {
        private Integer id;
        private String name;
        private Position position;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Position {
        private Double x;
        private Double y;
        private Double z;
    }
}