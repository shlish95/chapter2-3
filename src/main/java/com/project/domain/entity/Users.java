package com.project.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String password;
    private BigDecimal balance;
    private LocalDateTime createAt;

    protected Users() {}

    public Users(Long userId, String name, String password, BigDecimal balance, LocalDateTime createAt) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.createAt = createAt;
    }

    public Users(String name, String password) {
        this.name = name;
        this.password = password;
        this.balance = BigDecimal.ZERO;
    }

    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public BigDecimal getBalance() { return balance; }

    public void addBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.balance = this.balance.add(amount);
    }

    public void subtractBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("차감 금액은 0보다 커야 합니다.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
