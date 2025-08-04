package com.project.infrastructure.persistence.reservation;

import com.project.domain.entity.Reservation;
import com.project.domain.enums.ReservationStatus;
import com.project.interfaces.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaReservationRepository implements ReservationRepository {

    private final SpringDataReservationRepository repo;

    @Override
    public Reservation save(Reservation reservation) {
        return repo.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void updateStatus(Long reservationId, String status) {
        repo.updateStatus(reservationId, status);
    }

    @Override
    public boolean existsActiveReservationBySeatId(Long seatId) {
        return repo.existsBySeatIdAndStatusIn(seatId, List.of(ReservationStatus.HOLD, ReservationStatus.CONFIRMED));
    }

    @Override
    public void expireHoldReservationsBefore(LocalDateTime now) {
        repo.expireHoldReservationBefore(now);
    }

}
