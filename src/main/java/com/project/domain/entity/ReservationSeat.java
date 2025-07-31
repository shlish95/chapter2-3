package com.project.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_seat")
public class ReservationSeat {

    @EmbeddedId
    private ReservationSeatId id;
    private LocalDateTime expiresAt;

    protected  ReservationSeat() {}

    public ReservationSeat(Long reservationId, Long seatId, LocalDateTime expiresAt) {
        this.id = new ReservationSeatId(reservationId, seatId);
        this.expiresAt = expiresAt;
    }

    public ReservationSeatId getId() {
        return id;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public Long getSeatId() {
        return id.getSeatId();
    }
}
