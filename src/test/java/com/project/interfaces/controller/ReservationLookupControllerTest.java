package com.project.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.entity.Concert;
import com.project.domain.entity.Seat;
import com.project.domain.enums.ReservationStatus;
import com.project.infrastructure.persistence.reservation.SpringDataReservationRepository;
import com.project.infrastructure.persistence.reservationLookup.SpringDataConcertRepository;
import com.project.infrastructure.persistence.reservationSeat.SpringDataReservationSeatRepository;
import com.project.infrastructure.persistence.seat.SpringDataSeatRepository;
import kr.hhplus.be.server.ServerApplication;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ServerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationLookupControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpringDataConcertRepository concertRepo;
    @Autowired
    SpringDataSeatRepository seatRepo;
    @Autowired
    SpringDataReservationRepository reservationRepo;
    @Autowired
    SpringDataReservationSeatRepository reservationSeatRepo;

    LocalDate date;

    @BeforeEach
    void setUp() {
        reservationSeatRepo.deleteAll();
        reservationRepo.deleteAll();
        seatRepo.deleteAll();
        concertRepo.deleteAll();

        date = LocalDate.of(2025, 1, 1);
        Concert concert = concertRepo.save(new Concert("test_concert", date, 1L));

        IntStream.rangeClosed(1, 50).forEach(n ->
                seatRepo.save(new Seat(concert.getConcertId(), n, BigDecimal.valueOf(10_000)))
        );
    }

    @DisplayName("GET /api/concerts/dates - 에약 가능한 날짜 목록 반환")
    @Test
    void getAvailableDatesTest() throws Exception {
        mockMvc.perform(get("/api/concerts/dates")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(date.toString()));
    }

    @DisplayName("GET /api/concerts/{date}/seats - 해당 날짜의 예약 가능 좌석 목록 반환")
    @Test
    void getAvailableSeatByDateTest() throws Exception {
        mockMvc.perform(get("/api/concerts/{date}/seats", date)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(50))
                .andExpect(jsonPath("$[0].seatNum").value(1))
                .andExpect(jsonPath("$[0].price", Matchers.is(10_000.0)));
    }
}