package com.project.interfaces;

import com.project.domain.entity.Payment;

public interface PaymentRepositoryInterface {
    Payment save(Payment payment);
}
