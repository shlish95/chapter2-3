package com.project.infrastructure.persistence.reservationLookup;

import com.project.domain.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpringDataConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT DISTINCT c.date FROM Concert c ORDER BY c.date")
    List<LocalDate> findDistinctDates();

    Optional<Concert> findByDate(LocalDate date);
}
