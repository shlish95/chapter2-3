package com.project.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PayRequest(
        @JsonProperty("userId") Long userId,
        @JsonProperty("reservationId") Long reservationId
) {
}
