package com.project.domain.usecase;

import com.project.domain.entity.Users;

public interface CreateUserUseCase {
    Users create(String name, String password);
}
