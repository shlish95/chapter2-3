package com.project.infrastructure.persistence.reservationLookup;

import com.project.domain.entity.Concert;
import com.project.interfaces.ConcertRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaConcertRepository implements ConcertRepositoryInterface {

    private final SpringDataConcertRepository repo;

    @Override
    public List<LocalDate> findDistinctDates() {
        return repo.findDistinctDates();
    }

    @Override
    public Optional<Concert> findByDate(LocalDate date) {
        return repo.findByDate(date);
    }
}
