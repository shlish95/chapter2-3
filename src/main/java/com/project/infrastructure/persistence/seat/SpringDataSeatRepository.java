package com.project.infrastructure.persistence.seat;

import com.project.application.dto.SeatInfo;
import com.project.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataSeatRepository extends JpaRepository<Seat, Long> {

    @Query("""
            SELECT new com.project.application.dto.SeatInfo(s.seatNum, s.price)
            FROM Seat s
            JOIN Concert c ON c.concertId = s.concertId
            WHERE c.date = :date
              AND NOT EXISTS (
                  SELECT 1
                  FROM ReservationSeat rs
                  JOIN Reservation r ON r.reservationId = rs.id.reservationId
                  WHERE rs.id.seatId = s.seatId
                    AND (
                        r.status = com.project.domain.enums.ReservationStatus.CONFIRMED
                        OR (r.status = com.project.domain.enums.ReservationStatus.HOLD AND rs.expiresAt > :now)
                    )
                )
            ORDER BY s.seatNum
            """)
    List<SeatInfo> findAvailableSeatsByDate(@Param("date") LocalDate date, @Param("now") LocalDateTime now);
    Optional<Seat> findByConcertIdAndSeatNum(Long concertId, int seatNum);
}
