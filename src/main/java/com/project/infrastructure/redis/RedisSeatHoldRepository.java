package com.project.infrastructure.redis;

import com.project.interfaces.repository.SeatHoldRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@ConditionalOnBean(RedissonClient.class)
public class RedisSeatHoldRepository implements SeatHoldRepository {

    private final RedissonClient redissonClient;
    private static final String KEY_FMT = "seat:hold:%s:%d";

    @Override
    public boolean tryHold(String date, int seatNum, String userUuid, long ttlSeconds) {
        RBucket<String> bucket = redissonClient.getBucket(key(date, seatNum));
        return bucket.trySet(userUuid, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean releaseIfOwner(String date, int seatNum, String userUuid) {
        RBucket<String> bucket = redissonClient.getBucket(key(date, seatNum));
        String cur = bucket.get();
        if (userUuid.equals(cur)) {
            return bucket.delete();
        }
        return false;
    }

    @Override
    public String currentHolder(String date, int seatNum) {
        RBucket<String> bucket = redissonClient.getBucket(key(date, seatNum));
        return bucket.get();
    }

    private String key(String date, int seatNum) {
        return String.format(KEY_FMT, date, seatNum);
    }
}
