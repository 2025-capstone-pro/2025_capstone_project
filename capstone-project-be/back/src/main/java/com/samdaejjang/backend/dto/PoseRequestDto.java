package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PoseRequestDto {
    private List<FrameDataRequest.FrameDto> poseData;
    private List<Anomaly> anomalies;
}