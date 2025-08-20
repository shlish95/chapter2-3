package com.project.interfaces.dto;

import com.project.domain.enums.ReservationStatus;

public record PayResponse(Long reservationId, ReservationStatus reservationStatus) {
}
