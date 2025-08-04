package com.project.infrastructure.persistence.reservation;

import com.project.domain.entity.Reservation;
import com.project.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findById(Long reservationId);

    @Modifying
    @Query("""
                UPDATE Reservation r
                SET r.status = :status
                WHERE r.reservationId = :reservationId
            """)
    void updateStatus(
            @Param("reservationId") Long reservationId,
            @Param("status") String status
    );

    boolean existsBySeatIdAndStatusIn(Long seatId, List<ReservationStatus> status);

    @Modifying
    @Query("UPDATE Reservation  r SET r.status = 'CANCELLED' WHERE r.status = 'HOLD' AND r.expiresAt < :now")
    void expireHoldReservationBefore(@Param("now") LocalDateTime now);

}
