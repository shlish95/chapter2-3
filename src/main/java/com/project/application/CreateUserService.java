package com.project.application;

import com.project.domain.entity.Users;
import com.project.domain.usecase.CreateUserUseCase;
import com.project.interfaces.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepositoryInterface userRepository;

    public Users create(String name, String password) {
        Users user = new Users(name, password);
        return userRepository.save(user);
    }
}
