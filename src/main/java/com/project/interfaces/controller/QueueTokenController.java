package com.project.interfaces.controller;

import com.project.application.facade.QueueTokenFacade;
import com.project.domain.entity.QueueToken;
import com.project.interfaces.dto.QueueTokenRequest;
import com.project.interfaces.dto.QueueTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tokens")
public class QueueTokenController {

    private final QueueTokenFacade queueTokenFacade;

    @PostMapping
    public ResponseEntity<QueueTokenResponse> issue(@RequestBody @Valid QueueTokenRequest request) {
        QueueToken token = queueTokenFacade.issueOrReuse(request.userUuid());
        return ResponseEntity.ok(toResponse(token));
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<QueueTokenResponse> get(@PathVariable String userUuid) {
        QueueToken token = queueTokenFacade.getOrThrow(userUuid);
        return ResponseEntity.ok(toResponse(token));
    }

    private QueueTokenResponse toResponse(QueueToken token) {
        Integer rank = queueTokenFacade.currentRank(token.getUserUuid());
        long ttl = queueTokenFacade.remainingSeconds(token.getUserUuid());
        return new QueueTokenResponse(
                token.getUserUuid(),
                token.getQueuePosition(),
                token.getIssuedAt(),
                token.getExpiresAt(),
                rank,
                ttl
        );
    }
}
