package com.project.domain.entity;

import com.project.domain.enums.ReservationStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 참조면 true
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;

        // PK가 없으면 아직 영속화 전이므로 equals는 false
        if (this.reservationId == null || that.reservationId == null) {
            return false;
        }

        return Objects.equals(this.reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

}
