package com.project.application.reserve;

import com.project.application.exception.SeatNotFoundException;
import com.project.domain.entity.Seat;
import com.project.interfaces.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatQueryService {

    private final SeatRepository seatRepo;

    public Seat lockAndGetSeat(Long concertId, int seatNum) {
        return seatRepo.findByConcertIdAndSeatNumForUpdate(concertId, seatNum)
                .orElseThrow(() -> new SeatNotFoundException("좌석 없음: concertId = " + concertId + ", seatNum = " + seatNum));
    }
}
