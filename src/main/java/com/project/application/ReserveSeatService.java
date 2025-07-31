package com.project.application;

import com.project.domain.entity.*;
import com.project.domain.usecase.ReserveSeatUseCase;
import com.project.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReserveSeatService {

    private final QueueTokenRepositoryInterface tokenRepo;
    private final ConcertRepositoryInterface concertRepo;
    private final SeatRepositoryInterface seatRepo;
    private final ReservationRepositoryInterface reservationRepo;
    private final ReservationSeatRepositoryInterface reservationSeatRepo;

    public Reservation reserve(String userUuid, LocalDate date, int seatNum) {
        // 1) 토큰 검증
        QueueToken token = tokenRepo.findByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        Long userId = token.getUserId();

        // 2) 콘서트 조회
        Concert concert = concertRepo.findByDate(date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜에 콘서트가 없습니다."));

        // 3) 좌석 조회
        Seat seat = seatRepo.findByConcertIdAndSeatNum(concert.getConcertId(), seatNum)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));

        // 4) 애플리케이션 레벨 중복 검사
        if (reservationSeatRepo.existsHold(seat.getSeatId())) {
            throw new IllegalArgumentException("이미 점유된 좌석입니다.");
        }

        // 5) Reservation 저장
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(
                userId,
                concert.getConcertId(),
                "HOLD",
                now,
                now.plusMinutes(5)
        );
        Reservation saved = reservationRepo.save(reservation);

        // 6) ReservationSeat 저장
        ReservationSeat reservationSeat = new ReservationSeat(
                saved.getReservationId(),
                seat.getSeatId(),
                now.plusMinutes(5)
        );
        reservationSeatRepo.save(reservationSeat);

        return saved;
    }
}
