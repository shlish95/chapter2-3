package com.project.infrastructure.persistence.queueToken;

import com.project.domain.entity.QueueToken;
import com.project.interfaces.QueueTokenRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaQueueTokenRepository implements QueueTokenRepositoryInterface {

    private final SpringDataQueueTokenRepository tokenRepo;


    @Override
    public int nextQueuePosition() {
        Integer max = tokenRepo.findAll().stream()
                .mapToInt(QueueToken::getQueuePosition)
                .max()
                .orElse(0);
        return max + 1;
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
        tokenRepo.findByUserUuid(userUuid)
                .ifPresent(token -> {
                    token.expireNow();
                    tokenRepo.save(token);
                });
    }
}
