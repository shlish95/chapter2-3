package com.project.application;

import com.project.domain.entity.QueueToken;
import com.project.interfaces.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueTokenService {

    private final QueueTokenRepository tokenRepository;

    public QueueToken issue(String userUuid) {
        Optional<QueueToken> optionalToken = tokenRepository.findByUserUuid(userUuid);
        if (optionalToken.isPresent() && !optionalToken.get().isExpired()) {
            return tokenRepository.save(optionalToken.get());
        } else {
            int position = tokenRepository.nextQueuePosition();
            LocalDateTime now = LocalDateTime.now();
            QueueToken newToken = new QueueToken(position, userUuid, now, now.plusMinutes(1));
            return tokenRepository.save(newToken);
        }
    }

    public QueueToken getStatus(String userUuid) {
        return tokenRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 없습니다: " + userUuid));
    }

    public void expireToken(String userUuid) {
        tokenRepository.expire(userUuid);
    }

    public Long validateAndGetUseId(String userUuid) {
        QueueToken token = tokenRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));

        if (token.isExpired()) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        return token.getUserId();
    }
}
