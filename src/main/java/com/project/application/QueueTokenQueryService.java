package com.project.application;

import com.project.domain.entity.QueueToken;
import com.project.interfaces.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueTokenQueryService {

    private final QueueTokenRepository queueTokenRepo;

    public Optional<QueueToken> get(String userUuid) {
        return queueTokenRepo.findByUserUuid(userUuid);
    }

    public long reminingSeconds(String userUuid) {
        return queueTokenRepo.findByUserUuid(userUuid)
                .map(t -> Math.max(0L, Duration.between(LocalDateTime.now(), t.getExpiresAt()).getSeconds()))
                .orElse(0L);
    }

    public Integer currentRankByDb(String userUuid) {
        Optional<QueueToken> opt = queueTokenRepo.findByUserUuid(userUuid);
        if (opt.isEmpty() || opt.get().isExpired()) {
            return null;
        }
        QueueToken t = opt.get();
        int rank = queueTokenRepo.countActiveBefore(LocalDateTime.now(), t.getIssuedAt());
        return rank;
    }

    public int nextQueuePosition() {
        return queueTokenRepo.nextQueuePosition();
    }
}
