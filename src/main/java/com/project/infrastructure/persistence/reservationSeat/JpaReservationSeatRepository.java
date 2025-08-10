package com.project.infrastructure.persistence.reservationSeat;

import com.project.domain.entity.ReservationSeat;
import com.project.domain.enums.ReservationStatus;
import com.project.interfaces.repository.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaReservationSeatRepository implements ReservationSeatRepository {

    private final SpringDataReservationSeatRepository repo;

    @Override
    public List<ReservationSeat> findByReservationId(Long reservationId) {
        return repo.findById_ReservationId(reservationId);
    }

    @Override
    public ReservationSeat save(ReservationSeat reservationSeat) {
        return repo.save(reservationSeat);
    }

    @Override
    public boolean existsHold(Long seatId) {
        return repo.existsHold(seatId);
    }

    @Override
    public Optional<ReservationSeat> findActiveBySeatIdForUpdate(Long seatId) {
        return repo.findActiveBySeatIdForUpdate(
                seatId,
                List.of(ReservationStatus.HOLD, ReservationStatus.CONFIRMED),
                LocalDateTime.now());
    }
}
