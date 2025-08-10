package com.project.application.reserve;

import com.project.domain.entity.ReservationSeat;
import com.project.interfaces.repository.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationSeatCommandService {

    private final ReservationSeatRepository reservationSeatRepo;

    public ReservationSeat createTemporaryAllocation(Long reservationId, Long seatId, LocalDateTime expireAt) {
        ReservationSeat reservationSeat = new ReservationSeat(reservationId, seatId, expireAt);
        return reservationSeatRepo.save(reservationSeat);
    }
}
