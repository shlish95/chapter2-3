package com.project.application.service.concert;

import com.project.domain.entity.Concert;
import com.project.interfaces.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepo;

    public List<LocalDate> getAvailableDates() {
        return concertRepo.findDistinctDates();
    }

    public Concert findByDate(LocalDate date) {
        return concertRepo.findByDate(date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 콘서트가 없습니다."));
    }
}
