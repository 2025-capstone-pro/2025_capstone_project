package com.samdaejjang.backend.dto;

import lombok.Data;

@Data
public class Anomaly {
    private String joint;
    private int frame;
    private String severity;
}
