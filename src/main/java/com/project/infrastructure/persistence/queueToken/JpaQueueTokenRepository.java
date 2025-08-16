package com.project.infrastructure.persistence.queueToken;

import com.project.domain.entity.QueueToken;
import com.project.interfaces.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaQueueTokenRepository implements QueueTokenRepository {

    private final SpringDataQueueTokenRepository tokenRepo;

    @Override
    public int nextQueuePosition() {
        return tokenRepo.nextQueuePosition();
    }

    @Override
    public QueueToken save(QueueToken token) {
        return tokenRepo.save(token);
    }

    @Override
    public Optional<QueueToken> findByUserUuid(String userUuid) {
        return tokenRepo.findByUserUuid(userUuid);
    }

    @Override
    public void expire(String userUuid) {
        tokenRepo.expire(userUuid, LocalDateTime.now());
    }

    @Override
    public void deleteAll() {
        tokenRepo.deleteAll();
    }

    @Override
    public int countActiveBefore(LocalDateTime now, LocalDateTime issuedAt) {
        return tokenRepo.countActiveBefore(now, issuedAt);
    }
}
