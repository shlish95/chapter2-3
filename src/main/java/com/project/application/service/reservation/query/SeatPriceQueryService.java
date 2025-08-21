package com.project.application.service.reservation.query;

import com.project.domain.entity.Seat;
import com.project.interfaces.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SeatPriceQueryService {

    private final SeatRepository seatRepo;

    public BigDecimal getPriceBySeatId(Long seatId) {
        return seatRepo.findById(seatId)
                .map(Seat::getPrice)
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다. seatId = " + seatId));
    }
}
