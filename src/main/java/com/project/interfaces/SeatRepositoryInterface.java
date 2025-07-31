package com.project.interfaces;

import com.project.domain.dto.SeatInfo;
import com.project.domain.entity.Seat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeatRepositoryInterface {
    List<SeatInfo> findAvailableSeatsByDate(LocalDate date);
    Optional<Seat> findByConcertIdAndSeatNum(Long concertId, int seatNum);
    Optional<Seat> findById(Long seatId);
}
