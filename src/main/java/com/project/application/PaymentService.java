package com.project.application;

import com.project.domain.entity.*;
import com.project.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final QueueTokenRepositoryInterface tokenRepo;
    private final ReservationRepositoryInterface reservationRepo;
    private final ReservationSeatRepositoryInterface reservationSeatRepo;
    private final SeatRepositoryInterface seatRepo;
    private final UserRepositoryInterface userRepo;
    private final PaymentRepositoryInterface paymentRepo;

    public Payment pay(String userUuid, Long reservationId) {
        // 1) 토큰 검증
        QueueToken token = tokenRepo.findByUserUuid(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰 입니다."));

        // 2) Reservation 조회
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약이 없습니다."));

        // 3) ReservationSeat -> Seat 조회해서 금액 합산
        List<ReservationSeat> seats = reservationSeatRepo.findByReservationId(reservationId);
        if (seats.isEmpty()) {
            throw new IllegalStateException("예약된 좌석이 없습니다.");
        }
        BigDecimal total = seats.stream()
                .map(ReservationSeat::getSeatId)
                .map(seatId -> seatRepo.findById(seatId)
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 좌석입니다."))
                        .getPrice()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4) 유저 잔액 조회, 차감
        Users user = userRepo.findById(token.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        if (user.getBalance().compareTo(total) < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        user.deduct(total);
        userRepo.save(user);

        // 5) Payment 생성, 저장
        Payment payment = new Payment(
                reservationId,
                total,
                reservation.getStatus(),
                LocalDateTime.now()
        );
        Payment saved = paymentRepo.save(payment);

        // 6) Reservation 상태 변경, 토큰 만료
        reservationRepo.updateStatus(reservationId, "CONFIRMED");
        tokenRepo.expire(userUuid);

        return saved;
    }
}
