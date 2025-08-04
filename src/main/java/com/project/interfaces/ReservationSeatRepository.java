package com.project.interfaces;

import com.project.domain.entity.ReservationSeat;

import java.util.List;
import java.util.Optional;

public interface ReservationSeatRepository {
    List<ReservationSeat> findByReservationId(Long reservationId);
    ReservationSeat save(ReservationSeat reservationSeat);
    boolean existsHold(Long seatId);

    Optional<ReservationSeat> findActiveBySeatIdForUpdate(Long seatId);
}
