package com.project.infrastructure.persistence.reservationSeat;

import com.project.domain.entity.ReservationSeat;
import com.project.interfaces.ReservationSeatRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaReservationSeatRepository implements ReservationSeatRepositoryInterface {

    private final SpringDataReservationSeatRepository repo;

    @Override
    public List<ReservationSeat> findByReservationId(Long reservationId) {
        return repo.findByReservationId(reservationId);
    }

    @Override
    public ReservationSeat save(ReservationSeat reservationSeat) {
        return repo.save(reservationSeat);
    }

    @Override
    public boolean existsHold(Long seatId) {
        return repo.existsHold(seatId);
    }
}
