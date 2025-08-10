package com.project.application;

import com.project.domain.entity.Users;
import com.project.interfaces.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserRepository userRepository;

    public Users charge(Long userId, BigDecimal amount) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        user.addBalance(amount);
        return userRepository.save(user);
    }

    public BigDecimal getBalance(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."))
                .getBalance();
    }
}
