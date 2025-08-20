package com.project.application;

import com.project.domain.entity.Reservation;
import com.project.interfaces.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepo;

    public Reservation getOrThrow(Long reservationId) {
        return reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id= " + reservationId));
    }
}
