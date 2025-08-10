package com.project.application.reserve;

import com.project.application.exception.InsufficientBalanceException;
import com.project.domain.entity.Users;
import com.project.interfaces.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserBalanceService {

    private final UserRepository userRepo;

    public void checkAndDeductBalance(Long userId, BigDecimal amount) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다. 현재: " + user.getBalance() + ", 필요한 금액: " + amount);
        }

        user.subtractBalance(amount);
        userRepo.save(user);
    }
}
