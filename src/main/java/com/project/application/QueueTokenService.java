package com.project.application;

import com.project.domain.entity.QueueToken;
import com.project.domain.usecase.QueueTokenUseCase;
import com.project.interfaces.QueueTokenRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QueueTokenService {

    private final QueueTokenRepositoryInterface tokenRepository;

    public QueueToken issue(String userUuid) {
        int position = tokenRepository.nextQueuePosition();
        LocalDateTime now = LocalDateTime.now();
        QueueToken queueToken = new QueueToken(null, position, userUuid, now, now.plusMinutes(1));

        return tokenRepository.save(queueToken);
    }

    public QueueToken getStatus(String userUuid) {
        return tokenRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 없습니다: " + userUuid));
    }
}
