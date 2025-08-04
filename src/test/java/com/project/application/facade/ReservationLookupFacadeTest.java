package com.project.application.facade;

import com.project.application.ConcertService;
import com.project.application.SeatService;
import com.project.application.dto.SeatInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationLookupFacadeTest {

    @Mock
    ConcertService concertService;

    @Mock
    SeatService seatService;

    @InjectMocks
    ReservationLookupFacade reservationLookupFacade;

    @DisplayName("예약 가능 날짜 조회")
    @Test
    void getAvailableDatesTest() {
        //given
        List<LocalDate> expected = List.of(LocalDate.of(2025, 8, 1));
        given(concertService.getAvailableDates())
                .willReturn(expected);

        //when
        List<LocalDate> actual = reservationLookupFacade.getAvailableDates();

        //then
        then(concertService).should().getAvailableDates();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("예약 가능 자리 조회")
    @Test
    void getAvailableSeatsTest() {
        //given
        LocalDate date = LocalDate.of(2025, 8, 1);
        List<SeatInfo> expected = List.of();
        given(seatService.getAvailableSeats(date))
                .willReturn(expected);

        //when
        List<SeatInfo> actual = reservationLookupFacade.getAvailableSeats(date);

        //then
        then(seatService).should().getAvailableSeats(date);
        assertThat(actual).isEqualTo(expected);
    }
}