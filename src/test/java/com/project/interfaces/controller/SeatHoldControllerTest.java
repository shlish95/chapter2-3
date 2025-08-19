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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ServerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeatHoldControllerTest {

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

    Long concertId;
    LocalDate date;

    @BeforeEach
    void setUp() {
        reservationSeatRepo.deleteAll();
        reservationRepo.deleteAll();
        seatRepo.deleteAll();
        concertRepo.deleteAll();

        date = LocalDate.of(2025, 1, 2);
        Concert concert = concertRepo.save(new Concert("testConcert", date, 1L));
        concertId = concert.getConcertId();
        seatRepo.save(new Seat(concertId, 1, BigDecimal.valueOf(10_000)));
        seatRepo.save(new Seat(concertId, 2, BigDecimal.valueOf(10_000)));
    }

    @DisplayName("POST /api/reservations/holds - 좌석 홀드 성공 시 201과 HOLD 상태 반환")
    @Test
    void holdSeat_createdTest() throws Exception {
        //given
        Long userId = 100L;
        int seatNum = 1;
        Long seatId = seatRepo.findByConcertIdAndSeatNum(concertId, seatNum)
                .orElseThrow().getSeatId();

        String body = objectMapper.writeValueAsString(Map.of(
                "userId", userId,
                "date", date.toString(),
                "seatNum", seatNum
        ));

        //when
        MvcResult mvcResult = mockMvc.perform(post("/api/reservations/holds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationId").isNumber())
                .andExpect(jsonPath("$.seatNum").value(seatNum))
                .andExpect(jsonPath("$.reservationStatus").value(ReservationStatus.HOLD.toString()))
                .andExpect(jsonPath("$.reservationExpiresAt").isNotEmpty())
                .andReturn();

        //then
        String json = mvcResult.getResponse().getContentAsString();
        Map<?, ?> res = objectMapper.readValue(json, Map.class);
        assertThat(res.get("seatNum")).isEqualTo(seatNum);
    }

    @DisplayName("POST /api/reservations/holds - 이미 홀드된 동일 좌석이면 409 CONFLICT")
    @Test
    void holdSeat_conflictOnDuplicationTest() throws Exception {
        //given
        Long userId = 100L;
        int seatNum = 1;

        String body = objectMapper.writeValueAsString(Map.of(
                "userId", userId,
                "date", date.toString(),
                "seatNum", seatNum
        ));

        //when
        //then
        mockMvc.perform(post("/api/reservations/holds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/reservations/holds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());

    }

}