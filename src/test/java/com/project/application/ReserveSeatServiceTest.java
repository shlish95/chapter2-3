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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ReserveSeatServiceTest {

    @Mock
    QueueTokenRepositoryInterface tokenRepo;

    @Mock
    ConcertRepositoryInterface concertRepo;

    @Mock
    SeatRepositoryInterface seatRepo;

    @Mock
    ReservationRepositoryInterface reservationRepo;

    @Mock
    ReservationSeatRepositoryInterface reservationSeatRepo;

    @InjectMocks
    ReserveSeatService reserveService;

    @DisplayName("빈 좌석 예약 하면 Reservation 생성 및 ReservationSeat 생성")
    @Test
    void reserveSuccessTest() {
        //given
        String userUuid = UUID.randomUUID().toString();
        Long userId = 1L;
        LocalDate date = LocalDate.of(2025, 7, 1);
        int seatNum = 5;

        // 1) 토큰 조회
        QueueToken queueToken = new QueueToken(
                userId,
                1,
                userUuid,
                LocalDateTime.now().minusSeconds(1),
                LocalDateTime.now().plusMinutes(4)
        );
        given(tokenRepo.findByUserUuid(userUuid)).willReturn(Optional.of(queueToken));

        // 2) 콘서트 조회
        Concert concert = new Concert("concertA", date, 11L);
        given(concertRepo.findByDate(date)).willReturn(Optional.of(concert));

        // 3) 좌석 조회
        Seat seat = new Seat(concert.getConcertId(), seatNum, null);
        given(seatRepo.findByConcertIdAndSeatNum(concert.getConcertId(), seatNum))
                .willReturn(Optional.of(seat));

        // 4) 중복 점유 없음
        given(reservationSeatRepo.existsHold(seat.getSeatId())).willReturn(false);

        // 5) save 등장: 들어온인스턴스를 그대로 리턴
        given(reservationRepo.save(any(Reservation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(reservationSeatRepo.save(any(ReservationSeat.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        Reservation result = reserveService.reserve(userUuid, date, seatNum);

        //then
        then(reservationRepo).should().save(any(Reservation.class));
        then(reservationSeatRepo).should().save(any(ReservationSeat.class));

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getConcertId()).isEqualTo(concert.getConcertId());
        assertThat(result.getStatus()).isEqualTo("HOLD");
        assertThat(result.getReservedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(result.getExpiresAt()).isAfterOrEqualTo(LocalDateTime.now());
    }

    @DisplayName("이미 HOLD 중인 좌석은 예약할 수 없다.")
    @Test
    void reserveAlreadyHeld_ThrowTest() {
        //given
        String userUuid = UUID.randomUUID().toString();
        LocalDate date = LocalDate.of(2025, 7, 5);
        int seatNum = 10;

        QueueToken queueToken = new QueueToken(
                2L,
                2,
                userUuid,
                LocalDateTime.now().minusSeconds(1),
                LocalDateTime.now().plusMinutes(4)
        );
        Concert concert = new Concert("concertB", date, 8L);
        Seat seat = new Seat(concert.getConcertId(), seatNum, null);

        given(tokenRepo.findByUserUuid(userUuid)).willReturn(Optional.of(queueToken));
        given(concertRepo.findByDate(date)).willReturn(Optional.of(concert));
        given(seatRepo.findByConcertIdAndSeatNum(concert.getConcertId(), seatNum))
                .willReturn(Optional.of(seat));
        given(reservationSeatRepo.existsHold(seat.getSeatId())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> reserveService.reserve(userUuid, date, seatNum))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 점유된 좌석입니다.");
    }
}
