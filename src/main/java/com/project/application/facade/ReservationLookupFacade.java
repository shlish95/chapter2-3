package com.project.application.facade;

import com.project.application.ConcertService;
import com.project.application.SeatService;
import com.project.application.dto.SeatInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationLookupFacade {

    private final ConcertService concertService;
    private final SeatService seatService;

    public List<LocalDate> getAvailableDates() {
        return concertService.getAvailableDates();
    }

    public List<SeatInfo> getAvailableSeats(LocalDate date) {
        return seatService.getAvailableSeats(date);
    }
}
