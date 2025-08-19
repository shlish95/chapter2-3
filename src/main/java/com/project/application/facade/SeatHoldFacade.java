package com.project.application.facade;

import com.project.application.ConcertService;
import com.project.application.SeatService;
import com.project.application.dto.ReservationResult;
import com.project.application.exception.SeatAlreadyReservedException;
import com.project.application.reserve.ReservationCommandService;
import com.project.application.reserve.ReservationSeatCommandService;
import com.project.application.reserve.SeatReservationService;
import com.project.domain.entity.Concert;
import com.project.domain.entity.Reservation;
import com.project.domain.entity.Seat;
import com.project.domain.enums.ReservationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SeatHoldFacade {

    private static final Duration HOLD_TTL = Duration.ofMinutes(5);

    private final ConcertService concertService;
    private final SeatService seatService;
    private final SeatReservationService seatReservationService;
    private final ReservationCommandService reservationCommandService;
    private final ReservationSeatCommandService reservationSeatCommandService;

    /**
     * 좌석 임시 홀드(예약 요청) 흐름:
     * 1) 날짜로 콘서트 조회 → 좌석 조회
     * 2) 좌석이 이미 HOLD/CONFIRMED 상태면 409용 예외
     * 3) HOLD 예약 생성(5분 만료)
     * 4) 좌석-예약 매핑(만료시각 포함)
     * 5) 결과 DTO 반환
     */
    @Transactional
    public ReservationResult hold(Long userId, LocalDate date, int seatNum) {
        Concert concert = concertService.findByDate(date);
        Seat seat = seatService.findByConcertIdAndSeatNum(concert.getConcertId(), seatNum);

        if (seatReservationService.isSeatHeldOrReserved(seat.getSeatId())) {
            throw new SeatAlreadyReservedException("이미 홀드되었거나 예약된 좌석입니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(HOLD_TTL.toMinutes());

        Reservation reservation = reservationCommandService.create(
                userId, concert.getConcertId(), ReservationStatus.HOLD, now, expiresAt
        );

        reservationSeatCommandService.createTemporaryAllocation(
                reservation.getReservationId(), seat.getSeatId(), expiresAt
        );

        return new ReservationResult(
                reservation.getReservationId(),
                seatNum,
                expiresAt,
                reservation.getStatus()
        );
    }
}
