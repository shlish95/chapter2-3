package com.project.application.reserve;

import com.project.scheduler.ReservationExpireScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig  // 스프링 테스트 컨텍스트 설정 (필요 시 생략 가능)
@EnableScheduling   // 스케줄링 기능 활성화 (여기선 없어도 됨)
public class ReservationExpireSchedulerTest {

    private ReservationCommandService reservationCommandService;
    private ReservationExpireScheduler scheduler;

    @BeforeEach
    void setUp() {
        // ReservationCommandService를 가짜(mock)로 생성
        reservationCommandService = Mockito.mock(ReservationCommandService.class);
        // 가짜 서비스를 주입해서 스케줄러 생성
        scheduler = new ReservationExpireScheduler(reservationCommandService);
    }

    @Test
    void testExpireReservationsCalled() {
        // run() 실행 시 → expireReservations()가 호출되는지 검증
        scheduler.run();

        // 가짜 서비스의 expireReservations가 딱 1번 호출됐는지 확인
        verify(reservationCommandService, times(1)).expireReservations();
    }
}
