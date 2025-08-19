package com.project.interfaces.controller;

import com.project.application.dto.SeatInfo;
import com.project.application.facade.ReservationLookupFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ReservationLookupController {

    private final ReservationLookupFacade reservationLookupFacade;

    @GetMapping("/dates")
    public ResponseEntity<List<LocalDate>> dates() {
        return ResponseEntity.ok(reservationLookupFacade.getAvailableDates());
    }

    @GetMapping("/{date}/seats")
    public ResponseEntity<List<SeatInfo>> seats(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reservationLookupFacade.getAvailableSeats(date));
    }
}
