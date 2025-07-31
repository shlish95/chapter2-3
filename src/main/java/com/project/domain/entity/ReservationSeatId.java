package com.project.domain.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class ReservationSeatId implements Serializable {

    private Long reservationId;
    private Long seatId;

    protected ReservationSeatId() {}

    public ReservationSeatId(Long reservationId, Long seatId) {
        this.reservationId = reservationId;
        this.seatId = seatId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getSeatId() {
        return seatId;
    }
}
