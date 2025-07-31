package com.project.infrastructure.persistence.users;

import com.project.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<Users, Long> {
}
