package com.project.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record PayRequest(
        @NotNull @JsonProperty("userId") Long userId,
        @NotNull @JsonProperty("reservationId") Long reservationId
) {
}
