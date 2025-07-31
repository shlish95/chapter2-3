package com.project.infrastructure.persistence.reservationSeat;

import com.project.domain.entity.ReservationSeat;
import com.project.domain.entity.ReservationSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataReservationSeatRepository extends JpaRepository<ReservationSeat, ReservationSeatId> {

    @Query("""
        SELECT COUNT(r) > 0
        FROM ReservationSeat r
        WHERE r.id.seatId = :seatId
          AND r.expiresAt > CURRENT_TIMESTAMP 
        """)
    boolean existsHold(@Param("seatId") Long seatId);
    List<ReservationSeat> findByReservationId(Long reservationId);
}
