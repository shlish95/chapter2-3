package com.project.application;

import com.project.domain.entity.QueueToken;
import com.project.interfaces.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QueueTokenCommandService {

    private final QueueTokenRepository queueTokenRepo;

    public QueueToken issueNew(String userUuid, int queuePosition, Duration ttl) {
        LocalDateTime now = LocalDateTime.now();
        QueueToken token = new QueueToken(queuePosition, userUuid, now, now.plus(ttl));
        return queueTokenRepo.save(token);
    }

    public void expire(String userUuid) {
        queueTokenRepo.findByUserUuid(userUuid).ifPresent(t -> {
            if (!t.isExpired()) {
                t.expireNow();
                queueTokenRepo.save(t);
            }
        });
    }
}
