package com.project.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ChargeRequest(
        @NotNull @JsonProperty("userId") Long userId,
        @NotNull @Positive @JsonProperty("amount")BigDecimal amount
        ) {
}
