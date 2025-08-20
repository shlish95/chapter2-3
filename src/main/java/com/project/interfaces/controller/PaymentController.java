package com.project.interfaces.controller;

import com.project.application.facade.PaymentFacade;
import com.project.domain.enums.ReservationStatus;
import com.project.interfaces.dto.PayRequest;
import com.project.interfaces.dto.PayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping
    public ResponseEntity<PayResponse> pay(@RequestBody PayRequest req) {
        Long id = paymentFacade.pay(req.userId(), req.reservationId());
        return ResponseEntity.ok(new PayResponse(id, ReservationStatus.CONFIRMED));
    }
}
