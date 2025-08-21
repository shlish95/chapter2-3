package com.project.interfaces.controller;

import com.project.application.BalanceService;
import com.project.domain.entity.Users;
import com.project.interfaces.dto.ChargeRequest;
import com.project.interfaces.dto.ChargeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> charge(@RequestBody ChargeRequest req) {
        Users newBalance = balanceService.charge(req.userId(), req.amount());
        return ResponseEntity.ok(new ChargeResponse(req.userId(), newBalance.getBalance()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ChargeResponse> get(@PathVariable @Valid Long userId) {
        BigDecimal balance = balanceService.getBalance(userId);
        return ResponseEntity.ok(new ChargeResponse(userId, balance));
    }
}
