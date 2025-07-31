package com.project.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private Long reservationId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime paidAt;

    protected Payment() {}

    public Payment(Long reservationId, BigDecimal amount, String status, LocalDateTime paidAt) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
