package com.project.application.service.reservation.query;

import com.project.domain.entity.ReservationSeat;
import com.project.interfaces.repository.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationSeatQueryService {

    private final ReservationSeatRepository reservationSeatRepo;

    public ReservationSeat getByReservationId(Long reservationId) {
        return reservationSeatRepo.findByReservationId(reservationId)
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("좌석 매핑이 없습니다. reservationId = " + reservationId));
    }
}
