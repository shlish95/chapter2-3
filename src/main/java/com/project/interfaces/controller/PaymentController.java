package com.project.interfaces.controller;

import com.project.application.service.payment.PaymentFacade;
import com.project.application.service.queue.QueueTokenFacade;
import com.project.domain.enums.ReservationStatus;
import com.project.interfaces.dto.PayRequest;
import com.project.interfaces.dto.PayResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;
    private final QueueTokenFacade queueTokenFacade;

    @PostMapping
    public ResponseEntity<PayResponse> pay(
            @RequestHeader(value = "X-QUEUE-UUID", required = false) String queueUuid,
            @RequestBody @Valid PayRequest req) {
        Long id = paymentFacade.pay(req.userId(), req.reservationId());
        if (queueUuid != null && !queueUuid.isBlank()) {
            queueTokenFacade.expire(queueUuid);
        }
        return ResponseEntity.ok(new PayResponse(id, ReservationStatus.CONFIRMED));
    }
}
