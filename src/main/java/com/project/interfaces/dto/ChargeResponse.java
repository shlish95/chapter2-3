package com.project.interfaces.dto;

import java.math.BigDecimal;

public record ChargeResponse(Long userId, BigDecimal balance) {
}
