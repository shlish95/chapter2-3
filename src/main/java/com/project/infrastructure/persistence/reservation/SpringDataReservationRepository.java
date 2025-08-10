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

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reservation r
        JOIN ReservationSeat rs ON r.reservationId = rs.id.reservationId
        WHERE rs.id.seatId = :seatId AND r.status IN :statusList
    """)
    boolean existsReservationForSeat(@Param("seatId") Long seatId,
                                     @Param("statusList") List<ReservationStatus> statusList);

    @Modifying
    @Query("UPDATE Reservation  r SET r.status = 'CANCELLED' WHERE r.status = 'HOLD' AND r.expiresAt < :now")
    void expireHoldReservationBefore(@Param("now") LocalDateTime now);

}
