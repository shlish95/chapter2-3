package com.project.interfaces;

import com.project.domain.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryInterface {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    void updateStatus(Long reservationId, String status);
}
