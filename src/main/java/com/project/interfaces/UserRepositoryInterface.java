package com.project.interfaces;

import com.project.domain.entity.Users;

import java.util.Optional;

public interface UserRepositoryInterface {
    Users save(Users user);
    Optional<Users> findById(Long id);
}
