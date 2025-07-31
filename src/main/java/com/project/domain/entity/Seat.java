package com.project.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seat")
public class Seat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private Long concertId;
    private int seatNum;
    private BigDecimal price;

    protected Seat() {}

    public Seat(Long concertId, int seatNum, BigDecimal price) {
        this.concertId = concertId;
        this.seatNum = seatNum;
        this.price = price;
    }

    public Long getSeatId() {
        return seatId;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
