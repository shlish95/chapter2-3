package com.project.application.service.concert;

import com.project.application.dto.SeatInfo;
import com.project.domain.entity.Seat;
import com.project.interfaces.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepo;

    public List<SeatInfo> getAvailableSeats(LocalDate date) {
        return seatRepo.findAvailableSeatsByDate(date);
    }

    public Seat findByConcertIdAndSeatNum(Long concertId, int seatNum) {
        return seatRepo.findByConcertIdAndSeatNum(concertId, seatNum)
                .orElseThrow(() -> new IllegalArgumentException("좌석이 없습니다."));
    }

    public Seat findById(Long seatId) {
        return seatRepo.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("좌석이 없습니다. "));
    }
}
