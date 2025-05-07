package com.samdaejjang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BodySpecRequest {

    @JsonProperty("height_cm")
    private float heightCm;

    @JsonProperty("weight_kg")
    private float weightKg;

    @JsonProperty("skeletal_muscle_mass")
    private float skeletalMuscleMass;

    @JsonProperty("fat_mass")
    private float fatMass;
}
