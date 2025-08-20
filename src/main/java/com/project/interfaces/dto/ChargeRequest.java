package com.project.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ChargeRequest(
        @JsonProperty("userId") Long userId,
        @JsonProperty("amount")BigDecimal amount
        ) {
}
