package com.project.application;

import com.project.domain.entity.Users;
import com.project.domain.usecase.BalanceUseCase;
import com.project.interfaces.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserRepositoryInterface userRepository;

    public Users charge(Long userId, BigDecimal amount) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        user.charge(amount);
        return userRepository.save(user);
    }

    public BigDecimal getBalance(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."))
                .getBalance();
    }
}
