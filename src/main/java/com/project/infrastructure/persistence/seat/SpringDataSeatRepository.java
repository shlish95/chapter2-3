package com.project.infrastructure.persistence.seat;

import com.project.domain.dto.SeatInfo;
import com.project.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpringDataSeatRepository extends JpaRepository<Seat, Long> {

    @Query("""
            SELECT new com.project.domain.dto.SeatInfo(s.seatNum, s.price)
            FROM Seat s, Concert c
            WHERE s.concertId = c.concertId
              AND c.date = :date
              AND NOT EXISTS (
                          SELECT 1
                          FROM Reservation r
                          WHERE r.concertId = s.concertId
                            AND r.status = 'HOLD'
                            AND r.expiresAt > CURRENT_TIMESTAMP 
                          )
            ORDER BY s.seatNum
            """)
    List<SeatInfo> findAvailableSeatsByDate(LocalDate date);
    Optional<Seat> findByConcertIdAndSeatNum(Long concertId, int seatNum);
}
