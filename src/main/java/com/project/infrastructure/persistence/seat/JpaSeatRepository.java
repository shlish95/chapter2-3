package com.project.infrastructure.persistence.seat;

import com.project.application.dto.SeatInfo;
import com.project.domain.entity.Seat;
import com.project.interfaces.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSeatRepository implements SeatRepository {

    private final SpringDataSeatRepository repo;

    @Override
    public List<SeatInfo> findAvailableSeatsByDate(LocalDate date) {
        return repo.findAvailableSeatsByDate(date, LocalDateTime.now());
    }

    @Override
    public Optional<Seat> findByConcertIdAndSeatNum(Long concertId, int seatNum) {
        return repo.findByConcertIdAndSeatNum(concertId, seatNum);
    }

    @Override
    public Optional<Seat> findById(Long seatId) {
        return repo.findById(seatId);
    }

    @Override
    public Optional<Seat> findByConcertIdAndSeatNumForUpdate(Long concertId, int seatNum) {
        return repo.findByConcertIdAndSeatNum(concertId, seatNum);
    }
}
