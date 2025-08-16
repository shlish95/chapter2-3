package com.project.application;

import com.project.interfaces.repository.WaitQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@org.springframework.boot.autoconfigure.condition.ConditionalOnBean(WaitQueueRepository.class)
public class WaitQueueService {

//    private final WaitQueueRepository waitQueueRepo;
//
//    public void join(String userUuid, LocalDateTime issuedAt) {
//        waitQueueRepo.join(userUuid, issuedAt);
//    }
//
//    public Integer rank(String userUuid) {
//        return waitQueueRepo.rank(userUuid);
//    }
//
//    public void leave(String userUuid) {
//        waitQueueRepo.leave(userUuid);
//    }
}
