package com.project.domain.usecase;

import com.project.domain.entity.Reservation;

import java.time.LocalDate;

public interface ReserveSeatUseCase {
    Reservation reserve(String userUuid, LocalDate date, int seatNum);
}
