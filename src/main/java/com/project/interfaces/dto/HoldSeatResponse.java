package com.project.interfaces.dto;

import java.time.LocalDateTime;

public record HoldSeatResponse(
        Long reservationId,
        Long seatId,
        LocalDateTime expiredAt,
        String status
) {
}
