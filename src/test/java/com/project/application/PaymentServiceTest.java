package com.project.application;

import com.project.domain.entity.*;
import com.project.interfaces.*;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    QueueTokenRepositoryInterface tokenRepo;

    @Mock
    UserRepositoryInterface userRepo;

    @Mock
    ReservationRepositoryInterface reservationRepo;

    @Mock
    ReservationSeatRepositoryInterface reservationSeatRepo;

    @Mock
    SeatRepositoryInterface seatRepo;

    @Mock
    PaymentRepositoryInterface paymentRepo;

    @InjectMocks
    PaymentService paymentService;

    @DisplayName("결제 정상 처리")
    @Test
    void payment_SuccessTest() {
        //given
        String userUuid = UUID.randomUUID().toString();
        Long reservationId = 100L;
        Long userId = 1L;
        Long concertId = 10L;
        Long seatId1 = 101L;
        Long seatId2 = 102L;
        BigDecimal seatValue1 = BigDecimal.valueOf(5000);
        BigDecimal seatValue2 = BigDecimal.valueOf(7000);


        // 1) 유효 토큰
        QueueToken token = new QueueToken(
                userId,
                1,
                userUuid,
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().plusMinutes(4)
        );
        given(tokenRepo.findByUserUuid(userUuid))
                .willReturn(Optional.of(token));

        // 2) 예약 조회 (HOLD 상태)
        Reservation reservation = new Reservation(
                userId,
                concertId,
                "HOLD",
                LocalDateTime.now().minusMinutes(3),
                LocalDateTime.now().plusMinutes(2)
        );
        given(reservationRepo.findById(reservationId))
                .willReturn(Optional.of(reservation));

        // 3) ReservationSeat -> 두 좌석
        ReservationSeat reservationSeat1 = new ReservationSeat(
                reservationId,
                seatId1,
                LocalDateTime.now().plusMinutes(2)
        );
        ReservationSeat reservationSeat2 = new ReservationSeat(
                reservationId,
                seatId2,
                LocalDateTime.now().plusMinutes(2)
        );
        given(reservationSeatRepo.findByReservationId(reservationId))
                .willReturn(List.of(reservationSeat1, reservationSeat2));

        // 4) Seat 조회 -> 가격 각각 5000, 7000
        given(seatRepo.findById(seatId1))
                .willReturn(Optional.of((new Seat(concertId, 1, seatValue1))));
        given(seatRepo.findById(seatId2))
                .willReturn(Optional.of((new Seat(concertId, 2, seatValue2))));

        // 5) Users 잔액 세팅
        Users user = new Users("userA", "passwordA");
        user.charge(BigDecimal.valueOf(20000));
        given(userRepo.findById(userId))
                .willReturn(Optional.of(user));
        given(userRepo.save(any(Users.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // 6) Payment 저장 시 그대로 리턴
        given(paymentRepo.save(any(Payment.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


        //when
        Payment payment = paymentService.pay(userUuid, reservationId);


        //then
        assertThat(payment.getAmount()).isEqualByComparingTo(seatValue1.add(seatValue2));
        then(paymentRepo).should().save(any(Payment.class));
        then(reservationRepo).should().updateStatus(reservationId, "CONFIRMED");
        then(tokenRepo).should().expire(userUuid);
    }

    @DisplayName("결제 시 잔액 부족 ")
    @Test
    void pay_insufficientBalanceTest() {
        //given
        String userUuid = UUID.randomUUID().toString();
        Long reservationId = 200L;
        Long userId = 2L;
        Long seatId = 301L;

        // 1) 토큰
        QueueToken token = new QueueToken(
                userId,
                2,
                userUuid,
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().plusMinutes(4)
        );
        given(tokenRepo.findByUserUuid(userUuid))
                .willReturn(Optional.of(token));

        // 2) Reservation 조회
        Reservation reservation = new Reservation(
                userId,
                20L,
                "HOLD",
                LocalDateTime.now().minusMinutes(2),
                LocalDateTime.now().plusMinutes(3)
        );
        given(reservationRepo.findById(reservationId))
                .willReturn(Optional.of(reservation));

        // 3) ReservationSeat -> 한 좌석
        ReservationSeat reservationSeat = new ReservationSeat(
                reservationId,
                seatId,
                LocalDateTime.now().plusMinutes(3)
        );
        given(reservationSeatRepo.findByReservationId(reservationId))
                .willReturn(List.of(reservationSeat));

        // 4) Seat 가격 5000
        given(seatRepo.findById(seatId))
                .willReturn(Optional.of(new Seat(20L, 1, BigDecimal.valueOf(5000))));


        // 5) Users 잔액 3000 (부족)
        Users user = new Users("userB", "passwordB");
        user.charge(BigDecimal.valueOf(3000));
        given(userRepo.findById(userId))
                .willReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> paymentService.pay(userUuid, reservationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액이 부족합니다.");
    }
}