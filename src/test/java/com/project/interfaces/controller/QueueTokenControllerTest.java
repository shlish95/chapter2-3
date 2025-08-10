package com.project.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.domain.entity.QueueToken;
import com.project.domain.entity.Users;
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

import java.time.LocalDateTime;
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

    private static final String userUuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        queueTokenRepository.deleteAll();

    }

    @DisplayName("POST /api/tokens -> DB에 토큰 생성 후 반환")
    @Test
    void issueToken_createRecordAndReturnsDtoTest() throws Exception {
        //given
        QueueTokenRequest queueTokenRequest = new QueueTokenRequest(userUuid);
        String body = objectMapper.writeValueAsString(queueTokenRequest);

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

        QueueToken saved = queueTokenRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new AssertionError("토큰이 저장되지 않았습니다."));
        assertThat(saved.getUserUuid()).isEqualTo(userUuid);
        assertThat(saved.getQueuePosition()).isEqualTo(1);
    }

    @DisplayName("GET /api/tokens/{userUuid} -> DB 조회 후 JSON 반환")
    @Test
    void getTokenTest() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        int queuePosition = 5;

        QueueToken pre = new QueueToken(
                queuePosition,
                userUuid,
                now.minusMinutes(1),
                now.plusMinutes(4)
        );
        queueTokenRepository.save(pre);

        //when
        //then
        mockMvc.perform(get("/api/tokens/{userUuid}", userUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userUuid").value(userUuid))
                .andExpect(jsonPath("$.queuePosition").value(queuePosition))
                .andExpect(jsonPath("$.issuedAt").isNotEmpty())
                .andExpect(jsonPath("$.expiresAt").isNotEmpty());
    }


}