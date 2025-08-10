package com.project.interfaces.repository;

import com.project.domain.entity.Concert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<LocalDate> findDistinctDates();
    Optional<Concert> findByDate(LocalDate date);
}
