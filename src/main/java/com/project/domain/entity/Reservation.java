package com.project.domain.entity;

import com.project.domain.enums.ReservationStatus;
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
    @Enumerated(EnumType.STRING) private ReservationStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;

    protected Reservation() {}

    public Reservation(Long userId, Long concertId, ReservationStatus status, LocalDateTime reservedAt, LocalDateTime expiresAt) {
        this.userId = userId;
        this.concertId = concertId;
        this.status = status;
        this.reservedAt = reservedAt;
        this.expiresAt = expiresAt;
    }

    public void changeStatus(ReservationStatus status) {
        this.status = status;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
