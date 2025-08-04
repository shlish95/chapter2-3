package com.project.application.reserve;

import com.project.domain.entity.Reservation;
import com.project.domain.enums.ReservationStatus;
import com.project.interfaces.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationRepository reservationRepo;

    public Reservation createPendingReservation(Long userId, Long concertId, LocalDateTime now, LocalDateTime expiresAt) {
        Reservation reservation = new Reservation(
                userId,
                concertId,
                ReservationStatus.HOLD,
                now,
                expiresAt
        );
        return reservationRepo.save(reservation);
    }

    public void updateStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("예약을 찾을 수 없습니다. "));
        reservation.changeStatus(status);
        reservationRepo.save(reservation);
    }

    public void expireReservations() {
        reservationRepo.expireHoldReservationsBefore(LocalDateTime.now());
    }
}
