package com.project.interfaces.controller;

import com.project.application.QueueTokenService;
import com.project.domain.entity.QueueToken;
import com.project.interfaces.dto.QueueTokenRequest;
import com.project.interfaces.dto.QueueTokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokens")
public class QueueTokenController {

    private final QueueTokenService queueTokenService;

    public QueueTokenController(QueueTokenService queueTokenService) {
        this.queueTokenService = queueTokenService;
    }

    @PostMapping
    public ResponseEntity<QueueTokenResponse> issueToken(@RequestBody @Valid QueueTokenRequest request) {
        QueueToken token = queueTokenService.issue(request.userUuid());
        QueueTokenResponse response = new QueueTokenResponse(
                token.getUserUuid(),
                token.getQueuePosition(),
                token.getIssuedAt(),
                token.getExpiresAt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<QueueTokenResponse> getToken(@PathVariable String userUuid) {
        QueueToken token = queueTokenService.issue(userUuid);
        QueueTokenResponse response = new QueueTokenResponse(
                token.getUserUuid(),
                token.getQueuePosition(),
                token.getIssuedAt(),
                token.getExpiresAt()
        );

        return ResponseEntity.ok(response);
    }
}
