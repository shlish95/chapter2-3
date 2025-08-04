package com.project.scheduler;

import com.project.application.reserve.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationExpireScheduler {

    private final ReservationCommandService reservationCommandService;

    @Scheduled(fixedRate = 60000)
    public void run() {
        reservationCommandService.expireReservations();
    }
}
