package com.project.application.service.queue;

import com.project.domain.entity.QueueToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueTokenFacade {

    private static final Duration TOKEN_TTL = Duration.ofMinutes(30);

    private final QueueTokenCommandService queueTokenCommandService;
    private final QueueTokenQueryService queueTokenQueryService;

    public QueueToken issueOrReuse(String userUuid) {
        Optional<QueueToken> existing = queueTokenQueryService.get(userUuid);
        if (existing.isPresent() && !existing.get().isExpired()) {
            return existing.get();
        }

        int nextPos = queueTokenQueryService.nextQueuePosition();
        QueueToken token = queueTokenCommandService.issueNew(userUuid, nextPos, TOKEN_TTL);
        return token;
    }

    public QueueToken getOrThrow(String userUuid) {
        return queueTokenQueryService.get(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
    }

    public Integer currentRank(String userUuid) {
        return queueTokenQueryService.currentRankByDb(userUuid);
    }

    public long remainingSeconds(String userUuid) {
        return queueTokenQueryService.reminingSeconds(userUuid);
    }

    public void expire(String userUuid) {
        queueTokenCommandService.expire(userUuid);
    }

}
