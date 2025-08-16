package com.project.domain.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "queue_token",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_queue_token",
                columnNames = "userUuid"
        )
)
public class QueueToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private int queuePosition;

    @Column(nullable = false)
    private String userUuid;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    protected QueueToken() {
    }

    public QueueToken(int queuePosition, String userUuid, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.queuePosition = queuePosition;
        this.userUuid = userUuid;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public static QueueToken issue(int queuePosition, String userUuid, Duration ttl) {
        LocalDateTime now = LocalDateTime.now();
        return new QueueToken(queuePosition, userUuid, now, now.plus(ttl));
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public void expireNow() {
        this.expiresAt = LocalDateTime.now();
    }

    public String getUserUuid() {
        return userUuid;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
