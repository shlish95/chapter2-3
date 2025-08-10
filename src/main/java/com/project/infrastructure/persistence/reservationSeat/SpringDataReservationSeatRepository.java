package com.project.infrastructure.persistence.reservationSeat;

import com.project.domain.entity.ReservationSeat;
import com.project.domain.entity.ReservationSeatId;
import com.project.domain.enums.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataReservationSeatRepository extends JpaRepository<ReservationSeat, ReservationSeatId> {

    @Query("""
        SELECT COUNT(r) > 0
        FROM ReservationSeat r
        WHERE r.id.seatId = :seatId
          AND r.expiresAt > CURRENT_TIMESTAMP 
        """)
    boolean existsHold(@Param("seatId") Long seatId);

    List<ReservationSeat> findById_ReservationId(Long reservationId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT rs FROM ReservationSeat rs
    JOIN Reservation r ON rs.id.reservationId = r.reservationId
    WHERE rs.id.seatId = :seatId
      AND r.status IN :activeStatuses
      AND rs.expiresAt > :now
    """)
    Optional<ReservationSeat> findActiveBySeatIdForUpdate(
            @Param("seatId") Long seatId,
            @Param("activeStatuses") List<ReservationStatus> activeStatuses,
            @Param("now") LocalDateTime now
    );


}
