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
public class QueueTokenService {

    private static final Duration TOKEN_TTL = Duration.ofMinutes(30);

    private final QueueTokenRepository tokenRepository;

//    public QueueToken issue(String userUuid) {
//        Optional<QueueToken> existing = tokenRepository.findByUserUuid(userUuid);
//        if (existing.isPresent() && !existing.get().isExpired()) {
//            return existing.get();
//        }
//
//        int nextPos = tokenRepository.nextQueuePosition();
//        QueueToken newToken = QueueToken.issue(nextPos, userUuid, TOKEN_TTL);
//        return tokenRepository.save(newToken);
//    }

    public Optional<QueueToken> get(String userUuid) {
        return tokenRepository.findByUserUuid(userUuid);
    }

    public void expire(String userUuid) {
        tokenRepository.expire(userUuid);
    }

    public Long validateAndGetUserId(String userUuid) {
        QueueToken token = tokenRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));

        if (token.isExpired()) {
            throw new IllegalStateException("토큰이 만료 되었습니다.");
        }
        return token.getUserId();
    }
}
