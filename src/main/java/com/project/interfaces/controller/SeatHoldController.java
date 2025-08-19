package com.project.interfaces.controller;

import com.project.application.dto.ReservationResult;
import com.project.application.facade.SeatHoldFacade;
import com.project.interfaces.dto.HoldSeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations/holds")
public class SeatHoldController {

    private final SeatHoldFacade seatHoldFacade;

    @PostMapping
    public ResponseEntity<ReservationResult> hold(@RequestBody HoldSeatRequest req) {
        var result = seatHoldFacade.hold(req.userId(), req.date(), req.seatNum());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
