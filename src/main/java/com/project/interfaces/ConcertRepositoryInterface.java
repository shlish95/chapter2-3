package com.project.interfaces;

import com.project.domain.entity.Concert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConcertRepositoryInterface {
    List<LocalDate> findDistinctDates();
    Optional<Concert> findByDate(LocalDate date);
}
