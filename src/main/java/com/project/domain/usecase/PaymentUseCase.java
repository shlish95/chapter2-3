package com.project.domain.usecase;

import com.project.domain.entity.Payment;

public interface PaymentUseCase {
    Payment pay(String userUuid, Long reservationId);
}
