package com.project.domain.usecase;

import com.project.domain.entity.Users;

public interface BalanceUseCase {
    Users charge(Long userId, int  amount);
    int getBalance(Long userId);
}
