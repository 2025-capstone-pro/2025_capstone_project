package com.samdaejjang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class BodySpecResponse {

    @JsonProperty("body_spec_id")
    private Long bodySpecId;
}
