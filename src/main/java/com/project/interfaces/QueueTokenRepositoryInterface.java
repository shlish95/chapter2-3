package com.project.interfaces;

import com.project.domain.entity.QueueToken;

import java.util.Optional;

public interface QueueTokenRepositoryInterface {
    int nextQueuePosition();
    QueueToken save(QueueToken token);
    Optional<QueueToken> findByUserUuid(String userUuid);
    void expire(String userUuid);
}
