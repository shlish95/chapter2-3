package com.project.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.entity.Users;
import com.project.infrastructure.persistence.users.SpringDataUserRepository;
import com.project.interfaces.dto.ChargeRequest;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ServerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BalanceControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SpringDataUserRepository userRepo;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
    }

    @DisplayName("잔액 충전 조회")
    @Test
    void chargeAndGetTest() throws Exception {
        //given
        Users user = userRepo.save(new Users("testUser", "testPassword"));

        //when
        //then
        mockMvc.perform(post("/api/wallets/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ChargeRequest(user.getUserId(), BigDecimal.valueOf(10_000)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(10_000.0)));

        mockMvc.perform(get("/api/wallets/{userId}", user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(10_000.0)));
    }
}