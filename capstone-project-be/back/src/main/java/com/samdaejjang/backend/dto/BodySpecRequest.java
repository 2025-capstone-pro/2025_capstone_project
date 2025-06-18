package com.samdaejjang.backend.dto;

import lombok.Data;

@Data
public class BodySpecRequest {

    private float heightCm;

    private float weightKg;

    private float skeletalMuscleMass;

    private float fatMass;
}
