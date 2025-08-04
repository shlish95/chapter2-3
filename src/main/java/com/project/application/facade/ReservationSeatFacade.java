package com.project.application.facade;

import com.project.application.QueueTokenService;
import com.project.application.dto.ReservationResult;
import com.project.application.exception.SeatAlreadyReservedException;
import com.project.application.reserve.*;
import com.project.domain.entity.Reservation;
import com.project.domain.entity.ReservationSeat;
import com.project.domain.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationSeatFacade {

    private final QueueTokenService queueTokenService;
    private final SeatQueryService seatQueryService;
    private final SeatReservationService seatReservationService;
    private final ReservationCommandService reservationCommandService;
    private final ReservationSeatCommandService reservationSeatCommandService;
    private final UserBalanceService userBalanceService;

    private static final Duration HOLD_DURATION = Duration.ofMinutes(5);

    @Transactional
    public ReservationResult reserveSeat(Long concertId, int seatNum, String queueTokenUuid) {
        // 1. 큐 토큰 검증해서 userId 가져오기
        Long userId = queueTokenService.validateAndGetUseId(queueTokenUuid);

        // 2. 좌석 잠그고 가져오기
        Seat seat = seatQueryService.lockAndGetSeat(concertId, seatNum);

        // 3. 좌석이 이미 다른 예약에 임시 배정/확정된 상태인지 확인
        if (isSeatAlreadyHeldOrReserved(seat.getSeatId())) {
            throw new SeatAlreadyReservedException("이미 예약되거나 임시 보류 중인 좌석입니다: " + seatNum);
        }

        // 4. 가격 확인
        BigDecimal price = seat.getPrice();
        if (price == null) {
            throw new IllegalStateException("좌석 가격이 설정되지 않았습니다.");
        }

        // 5. 유저 잔액 확인 및 차감
        userBalanceService.checkAndDeductBalance(userId, price);

        // 6. 예약 생성 (PENDING 또는 HOLD 상태)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationExpiresAt = now.plus(HOLD_DURATION);
        Reservation reservation = reservationCommandService.createPendingReservation(userId, concertId, now, reservationExpiresAt);

        // 7. ReservationSeat 생성 (임시 할당)
        ReservationSeat reservationSeat = reservationSeatCommandService.createTemporaryAllocation(
                reservation.getReservationId(),
                seat.getSeatId(),
                reservationExpiresAt
        );

        // 8.예약 만료 처리는 스케줄러를 통해 주기적으로 실행됩니다.
        // 현재는 ReservationExpireScheduler가 처리하며, 추후 Redis TTL 방식으로 대체 가능.

        return new ReservationResult(
                reservation.getReservationId(),
                seatNum,
                reservationExpiresAt,
                reservation.getStatus()
        );
    }

    private boolean isSeatAlreadyHeldOrReserved(Long seatId) {
        return seatReservationService.isSeatHeldOrReserved(seatId);
    }
}
