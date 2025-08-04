package com.project.application.reserve;

import com.project.interfaces.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatReservationService {

    private final ReservationRepository reservationRepo;

    public boolean isSeatHeldOrReserved(Long seatId) {
        return reservationRepo.existsActiveReservationBySeatId(seatId);
    }
}
