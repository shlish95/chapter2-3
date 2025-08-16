package com.project.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.interfaces.dto.QueueTokenRequest;
import com.project.interfaces.repository.QueueTokenRepository;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ServerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QueueTokenControllerTest  {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    QueueTokenRepository queueTokenRepository;

    @BeforeEach
    void setUp() {
        queueTokenRepository.deleteAll();
    }

    @DisplayName("POST /api/tokens - 토큰 발급 (처음 발급 시 대기번호=1)")
    @Test
    void issueTokenTest() throws Exception {
        //given
        String userUuid = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(new QueueTokenRequest(userUuid));

        //when
        //then
        mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userUuid").value(userUuid))
                .andExpect(jsonPath("$.queuePosition").value(1))
                .andExpect(jsonPath("$.issuedAt").isNotEmpty())
                .andExpect(jsonPath("$.expiresAt").isNotEmpty());
    }

    @DisplayName("GET /api/tokens/{userUuid} - 기존 토큰 조회 (없으면 404)")
    @Test
    void getTokenTest() throws Exception {
        //given
        String userUuid = UUID.randomUUID().toString();

        //when
        mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new QueueTokenRequest(userUuid))))
                .andExpect(status().isOk());

        //then
        mockMvc.perform(get("/api/tokens/{userUuid}", userUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userUuid").value(userUuid))
                .andExpect(jsonPath("$.queuePosition").value(1));
    }

    @DisplayName("POST /api/tokens - 이미 유효한 토큰이 있으면 재사용")
    @Test
    void issueReusesExistingTest() throws Exception {
        //given
        String userUuid = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(new QueueTokenRequest(userUuid));

        //when
        var res1 = mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        var res2 = mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String content1 = res1.getResponse().getContentAsString();
        String content2 = res2.getResponse().getContentAsString();
        assertThat(content1).isEqualTo(content2);
    }
}