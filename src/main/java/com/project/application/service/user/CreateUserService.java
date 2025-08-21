package com.project.application.service.user;

import com.project.domain.entity.Users;
import com.project.interfaces.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;

    public Users create(String name, String password) {
        Users user = new Users(name, password);
        return userRepository.save(user);
    }
}
