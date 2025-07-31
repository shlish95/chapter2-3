package com.project.infrastructure.persistence.queueToken;

import com.project.domain.entity.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataQueueTokenRepository extends JpaRepository<QueueToken, Long> {
    Optional<QueueToken> findByUserUuid(String userUuid);
}
