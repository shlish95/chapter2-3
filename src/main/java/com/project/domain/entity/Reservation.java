package com.project.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private Long userId;
    private Long concertId;
    private String status;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;

    protected Reservation() {}

    public Reservation(Long userId, Long concertId, String status, LocalDateTime reservedAt, LocalDateTime expiresAt) {
        this.userId = userId;
        this.concertId = concertId;
        this.status = status;
        this.reservedAt = reservedAt;
        this.expiresAt = expiresAt;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getConcertId() {
        return concertId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
