package com.project.domain.usecase;

import com.project.domain.dto.SeatInfo;

import java.time.LocalDate;
import java.util.List;

public interface ReservationLookupUseCase {

    List<LocalDate> getAvailableDates();

    List<SeatInfo> getAvailableSeats(LocalDate date);
}
