package com.project.interfaces;

import com.project.domain.entity.ReservationSeat;

import java.util.List;

public interface ReservationSeatRepositoryInterface {
    List<ReservationSeat> findByReservationId(Long reservationId);
    ReservationSeat save(ReservationSeat reservationSeat);
    boolean existsHold(Long seatId);
}
