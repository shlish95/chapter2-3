package com.project.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_token")
public class QueueToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private int queuePosition;
    private String userUuid;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    protected QueueToken() {}

    public QueueToken(Long userId, int queuePosition, String userUuid, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.userId = userId;
        this.queuePosition = queuePosition;
        this.userUuid = userUuid;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public void expireNow() {
        this.expiresAt = LocalDateTime.now();
    }

    public String getUserUuid() { return userUuid; }

    public int getQueuePosition() { return queuePosition; }

    public Long getUserId() { return userId; }
}
