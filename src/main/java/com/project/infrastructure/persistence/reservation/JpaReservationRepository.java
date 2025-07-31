package com.project.infrastructure.persistence.reservation;

import com.project.domain.entity.Reservation;
import com.project.interfaces.ReservationRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaReservationRepository implements ReservationRepositoryInterface {

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
}
