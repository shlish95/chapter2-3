package com.project.domain.usecase;

import com.project.domain.entity.QueueToken;

public interface QueueTokenUseCase {
    QueueToken issue(String userUuid);
    QueueToken getStatus(String userUuid);
}
