package com.project.interfaces.repository;

import com.project.domain.entity.Users;

import java.util.Optional;

public interface UserRepository {
    Users save(Users user);
    Optional<Users> findById(Long id);

    void deleteAll();
}
