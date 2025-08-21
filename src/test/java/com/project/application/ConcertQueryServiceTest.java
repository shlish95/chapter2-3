package com.project.application;

import com.project.application.dto.SeatInfo;
import com.project.application.service.concert.ConcertQueryService;
import com.project.interfaces.repository.ConcertRepository;
import com.project.interfaces.repository.SeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertQueryServiceTest {

    @Mock
    ConcertRepository concertRepo;

    @Mock
    SeatRepository seatRepo;

    @InjectMocks
    ConcertQueryService lookupService;

    @DisplayName("예약 가능 날짜 조회")
    @Test
    void getAvailableDatesTest() {
        //given
        LocalDate date1 = LocalDate.of(2025, 7, 1);
        LocalDate date2 = LocalDate.of(2025, 7, 2);

        given(concertRepo.findDistinctDates()).willReturn(List.of(date1, date2));

        //when
        List<LocalDate> dates = lookupService.getAvailableDates();

        //then
        assertThat(dates).containsExactlyInAnyOrder(date1, date2);
    }

    @DisplayName("특정 날짜의 예약 가능 좌석 조회")
    @Test
    void getAvailableSeatsTest() {
        //given
        LocalDate date = LocalDate.of(2025, 7, 1);

        List<SeatInfo> seatInfos = List.of(
                new SeatInfo(1, BigDecimal.valueOf(1000)),
                new SeatInfo(2, BigDecimal.valueOf(15000))
        );

        given(seatRepo.findAvailableSeatsByDate(date)).willReturn(seatInfos);

        //when
        List<SeatInfo> result = lookupService.getAvailableSeats(date);

        //then
        assertThat(result).hasSize(2)
                .extracting(SeatInfo::seatNum)
                .containsExactlyInAnyOrder(1, 2);
    }
}