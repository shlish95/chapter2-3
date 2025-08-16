package com.project.infrastructure.redis;

import com.project.interfaces.repository.WaitQueueRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Repository
@RequiredArgsConstructor
@org.springframework.boot.autoconfigure.condition.ConditionalOnBean(org.redisson.api.RedissonClient.class)
public class RedisWaitQueueRepository implements WaitQueueRepository {
//
//    private final RedissonClient redissonClient;
//    private static final String KEY = "queue:wait";
//
//    @Override
//    public void join(String userUuid, LocalDateTime issuedAt) {
//        double score = issuedAt.toInstant(ZoneOffset.UTC).toEpochMilli();
//        redissonClient.getScoredSortedSet(KEY).add(score, userUuid);
//    }
//
//    @Override
//    public Integer rank(String userUuid) {
//        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(KEY);
//        Integer r = zset.rank(userUuid);
//        return (r == null) ? null : r + 1;
//    }
//
//    @Override
//    public void leave(String userUuid) {
//        redissonClient.getScoredSortedSet(KEY).remove(userUuid);
//    }
//
//    @Override
//    public void reset() {
//        redissonClient.getKeys().delete(KEY);
//    }
}
