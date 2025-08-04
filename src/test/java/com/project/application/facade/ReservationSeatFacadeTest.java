package com.project.application.facade;

import com.project.application.QueueTokenService;
import com.project.application.dto.ReservationResult;
import com.project.application.exception.InsufficientBalanceException;
import com.project.application.exception.SeatAlreadyReservedException;
import com.project.application.reserve.*;
import com.project.domain.entity.Reservation;
import com.project.domain.entity.ReservationSeat;
import com.project.domain.entity.Seat;
import com.project.domain.enums.ReservationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationSeatFacadeTest {

    @Mock
    QueueTokenService queueTokenService;

    @Mock
    SeatQueryService seatQueryService;

    @Mock
    SeatReservationService seatReservationService;

    @Mock
    ReservationCommandService reservationCommandService;

    @Mock
    ReservationSeatCommandService reservationSeatCommandService;

    @Mock
    UserBalanceService userBalanceService;

    @InjectMocks
    ReservationSeatFacade reservationSeatFacade;

    @DisplayName("좌석 예약")
    @Test
    void ReservationSeatTest() {
        //given
        String queueToken = UUID.randomUUID().toString();
        String userUuid = UUID.randomUUID().toString();
        Long userId = 111L;
        Long concertId = 11L;
        int seatNum = 1;
        Long seatId = 11L;
        Long reservationId = 11L;
        BigDecimal price = BigDecimal.valueOf(10000);
        ReservationStatus reservationStatus = ReservationStatus.HOLD;

        Seat seat = mock(Seat.class);
        given(seat.getSeatId()).willReturn(seatId);
        given(seat.getPrice()).willReturn(price);
        given(seatQueryService.lockAndGetSeat(concertId, seatNum)).willReturn(seat);

        // token -> userId
        given(queueTokenService.validateAndGetUseId(queueToken)).willReturn(userId);

        // seat not held/reserved
        given(seatReservationService.isSeatHeldOrReserved(seatId)).willReturn(false);

        // reservation returned
        Reservation reservation = mock(Reservation.class);
        given(reservation.getReservationId()).willReturn(reservationId);
        given(reservation.getStatus()).willReturn(reservationStatus);
        given(reservationCommandService.createPendingReservation(
                eq(userId),
                eq(concertId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(reservation);

        // reservation seat
        ReservationSeat reservationSeat = mock(ReservationSeat.class);
        given(reservationSeatCommandService.createTemporaryAllocation(
                eq(reservationId),
                eq(seatId),
                any(LocalDateTime.class)
        )).willReturn(reservationSeat);


        //when
        ReservationResult result = reservationSeatFacade.reserveSeat(concertId, seatNum, queueToken);

        //then
        assertThat(result).isNotNull();
        assertThat(result.reservationId()).isEqualTo(reservationId);
        assertThat(result.seatNum()).isEqualTo(seatNum);
        assertThat(result.reservationStatus()).isEqualTo(reservationStatus);

        then(queueTokenService).should().validateAndGetUseId(queueToken);
        then(seatQueryService).should().lockAndGetSeat(concertId, seatNum);
        then(seatReservationService).should().isSeatHeldOrReserved(seatId);
        then(reservationCommandService).should().createPendingReservation(
                eq(userId),
                eq(concertId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @DisplayName("이미 보류/예약 중이면 에외 처리 ")
    @Test
    void reservationSeat_seatAlreadyHeldTest() {
        //given
        String queueToken = UUID.randomUUID().toString();
        String userUuid = UUID.randomUUID().toString();
        Long userId = 222L;
        Long concertId = 22L;
        int seatNum = 2;
        Long seatId = 22L;

        Seat seat = mock(Seat.class);
        given(seat.getSeatId()).willReturn(seatId);
        given(seatQueryService.lockAndGetSeat(concertId, seatNum)).willReturn(seat);
        given(queueTokenService.validateAndGetUseId(queueToken)).willReturn(userId);
        given(seatReservationService.isSeatHeldOrReserved(seatId)).willReturn(true);

        //when
        //then
        assertThrows(SeatAlreadyReservedException.class,
                () -> reservationSeatFacade.reserveSeat(concertId, seatNum, queueToken));
        then(seatReservationService).should().isSeatHeldOrReserved(seatId);
        then(userBalanceService).should(never()).checkAndDeductBalance(anyLong(), any());
        then(reservationCommandService).should(never()).createPendingReservation(anyLong(), any(), any(), any());
    }

    @DisplayName("잔액 부족이면 예외 처리")
    @Test
    void reservationSeat_insufficientBalanceTest() {
        //given
        String queueToken = UUID.randomUUID().toString();
        String userUuid = UUID.randomUUID().toString();
        Long userId = 333L;
        Long concertId = 33L;
        int seatNum = 3;
        Long seatId = 33L;
        Long reservationId = 33L;
        BigDecimal price = BigDecimal.valueOf(10000);

        Seat seat = mock(Seat.class);
        given(seat.getSeatId()).willReturn(seatId);
        given(seat.getPrice()).willReturn(price);
        given(seatQueryService.lockAndGetSeat(concertId, seatNum)).willReturn(seat);
        given(queueTokenService.validateAndGetUseId(queueToken)).willReturn(userId);
        given(seatReservationService.isSeatHeldOrReserved(seatId)).willReturn(false);

        willThrow(new InsufficientBalanceException("잔액 부족"))
                .given(userBalanceService).checkAndDeductBalance(userId, price);


        //when
        //then
        assertThrows(InsufficientBalanceException.class,
                () -> reservationSeatFacade.reserveSeat(concertId, seatNum, queueToken));
        then(userBalanceService).should().checkAndDeductBalance(userId, price);
        then(reservationCommandService).should(never()).createPendingReservation(anyLong(), any(), any(), any());
    }
}