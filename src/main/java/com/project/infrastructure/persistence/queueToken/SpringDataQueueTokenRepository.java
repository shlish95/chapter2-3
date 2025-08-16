package com.project.infrastructure.persistence.queueToken;

import com.project.domain.entity.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SpringDataQueueTokenRepository extends JpaRepository<QueueToken, Long> {
    Optional<QueueToken> findByUserUuid(String userUuid);

    @Query("SELECT coalesce(max(q.queuePosition), 0) + 1 from QueueToken q ")
    int nextQueuePosition();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update QueueToken q set q.expiresAt = :now where q.userUuid = :userUuid")
    int expire(@Param("userUuid") String userUuid, @Param("now")LocalDateTime now);

    @Query("""
        SELECT count(t) 
        from QueueToken t 
        where t.expiresAt > :now and t.issuedAt <= :issuedAt 
        """)
    int countActiveBefore(@Param("now") LocalDateTime now, @Param("issuedAt") LocalDateTime issuedAt);
}
