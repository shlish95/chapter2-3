package com.project.interfaces.repository;

import com.project.domain.entity.Reservation;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    void updateStatus(Long reservationId, String status);
    boolean existsActiveReservationBySeatId(Long seatId);

    void expireHoldReservationsBefore(LocalDateTime now);
}
