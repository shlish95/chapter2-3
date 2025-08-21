package com.project.application.service.concert;

import com.project.application.dto.SeatInfo;
import com.project.interfaces.repository.ConcertRepository;
import com.project.interfaces.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertQueryService {

    private final ConcertRepository concertRepo;
    private final SeatRepository seatRepo;

    public List<LocalDate> getAvailableDates() {
        return concertRepo.findDistinctDates();
    }

    public List<SeatInfo> getAvailableSeats(LocalDate date) {
        return seatRepo.findAvailableSeatsByDate(date);
    }
}
