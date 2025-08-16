package com.project.interfaces.repository;

import com.project.domain.entity.QueueToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QueueTokenRepository {
    int nextQueuePosition();
    QueueToken save(QueueToken token);
    Optional<QueueToken> findByUserUuid(String userUuid);
    void expire(String userUuid);
    void deleteAll();
    int countActiveBefore(LocalDateTime now, LocalDateTime issuedAt);
}
