package com.project.interfaces.dto;

import java.time.LocalDateTime;

public record QueueTokenResponse(
        String userUuid,
        int queuePosition,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt,
        Integer currentRank,
        Long remainingSeconds) {
}
