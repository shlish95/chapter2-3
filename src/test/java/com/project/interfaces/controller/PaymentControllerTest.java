package com.project.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.entity.Concert;
import com.project.domain.entity.Seat;
import com.project.domain.entity.Users;
import com.project.domain.enums.ReservationStatus;
import com.project.infrastructure.persistence.reservation.SpringDataReservationRepository;
import com.project.infrastructure.persistence.reservationLookup.SpringDataConcertRepository;
import com.project.infrastructure.persistence.reservationSeat.SpringDataReservationSeatRepository;
import com.project.infrastructure.persistence.seat.SpringDataSeatRepository;
import com.project.infrastructure.persistence.users.SpringDataUserRepository;
import com.project.interfaces.dto.ChargeRequest;
import com.project.interfaces.dto.HoldSeatRequest;
import com.project.interfaces.dto.PayRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ServerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpringDataUserRepository userRepo;
    @Autowired
    SpringDataConcertRepository concertRepo;
    @Autowired
    SpringDataSeatRepository seatRepo;
    @Autowired
    SpringDataReservationRepository reservationRepo;
    @Autowired
    SpringDataReservationSeatRepository reservationSeatRepo;

    @BeforeEach
    void setUp() {
        reservationSeatRepo.deleteAll();
        reservationRepo.deleteAll();
        seatRepo.deleteAll();
        concertRepo.deleteAll();
        userRepo.deleteAll();
    }

    @DisplayName("결제하면 상태 HOLD -> CONFIRM")
    @Test
    void payTest() throws Exception {
        //given
        Users user = userRepo.save(new Users("testUser", "testPass"));

        LocalDate date = LocalDate.of(2025, 1, 3);
        Concert concert = concertRepo.save(new Concert("testConcert", date, 1L));
        seatRepo.save(new Seat(concert.getConcertId(), 1, BigDecimal.valueOf(10_000.0)));

        //when
        //then
        mockMvc.perform(post("/api/wallets/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ChargeRequest(user.getUserId(), BigDecimal.valueOf(20_000.0)))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/reservations/holds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new HoldSeatRequest(user.getUserId(), date, 1))))
                .andExpect(status().isCreated());

        Long reservationId = reservationRepo.findAll().get(0).getReservationId();

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PayRequest(user.getUserId(), reservationId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(reservationId))
                .andExpect(jsonPath("$.reservationStatus").value(ReservationStatus.CONFIRMED.name()));
    }

}