package com.project.application.facade;

import com.project.application.BalanceService;
import com.project.application.ReservationQueryService;
import com.project.application.ReservationSeatQueryService;
import com.project.application.SeatPriceQueryService;
import com.project.application.reserve.ReservationCommandService;
import com.project.domain.entity.Reservation;
import com.project.domain.entity.ReservationSeat;
import com.project.domain.enums.ReservationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final ReservationSeatQueryService reservationSeatQueryService;
    private final ReservationQueryService reservationQueryService;
    private final SeatPriceQueryService seatPriceQueryService;
    private final BalanceService balanceService;
    private final ReservationCommandService reservationCommandService;

    /**
     * 결제 흐름:
     * 1) reservationId로 HOLD 검증(+만료 아님, 좌석 소유자一致)
     * 2) 좌석 가격 합 계산 (지금은 1좌석)
     * 3) 잔액 차감
     * 4) 예약을 CONFIRMED로 변경
     */
    @Transactional
    public Long pay(Long userId, Long reservationId) {
        ReservationSeat reservationSeat = reservationSeatQueryService.getByReservationId(reservationId);
        Reservation reservation = reservationQueryService.getOrThrow(reservationId);

        if (reservation.getStatus() != ReservationStatus.HOLD) {
            throw new IllegalStateException("결재 가능한 상태가 아닙니다.");
        }
        if (reservation.getExpiresAt() != null && reservation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("임시 배정 시간이 만료 되었습니다.");
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalStateException("유저 불일치");
        }

        BigDecimal price = seatPriceQueryService.getPriceBySeatId(reservationSeat.getSeatId());
        balanceService.pay(userId, price);
        reservationCommandService.updateStatus(reservationId, ReservationStatus.CONFIRMED);
        return reservationId;
    }
}
