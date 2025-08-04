package com.project.application.dto;

import com.project.domain.enums.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResult(
        Long reservationId,
        int seatNum,
        LocalDateTime reservationExpiresAt,
        ReservationStatus reservationStatus) {
}

