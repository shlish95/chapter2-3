package com.project.application;

import com.project.domain.entity.QueueToken;
import com.project.interfaces.QueueTokenRepositoryInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class QueueTokenServiceTest {

    @Mock
    private QueueTokenRepositoryInterface tokenRepo;

    @InjectMocks
    private QueueTokenService tokenService;

    @DisplayName("토큰 발급 시 새로운 QueueToken 저장하고 반환 ")
    @Test
    void issue_createAndSaveTokenTest() {
        //given
        String userUuid = UUID.randomUUID().toString();

        given(tokenRepo.nextQueuePosition()).willReturn(1);
        given(tokenRepo.save(any(QueueToken.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when
        QueueToken issuedToken = tokenService.issue(userUuid);

        //then
        then(tokenRepo).should().save(any(QueueToken.class));
        assertThat(issuedToken.getUserUuid()).isEqualTo(userUuid);
        assertThat(issuedToken.getQueuePosition()).isEqualTo(1);
    }

    @DisplayName("토큰 조회 시 저장된 QueueToken 반환 ")
    @Test
    void getStatus_returnExistingTokenTest() {
        //given
        String userUuid = UUID.randomUUID().toString();
        QueueToken queueToken = new QueueToken(
                1L,
                5,
                userUuid,
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().plusMinutes(4)
        );

        given(tokenRepo.findByUserUuid(userUuid)).willReturn(Optional.of(queueToken));

        //when
        QueueToken returnedToken = tokenService.getStatus(userUuid);

        //then
        assertThat(returnedToken.getUserUuid()).isEqualTo(userUuid);
        assertThat(returnedToken.getQueuePosition()).isEqualTo(queueToken.getQueuePosition());
    }

    @DisplayName("토큰 조회 시 토큰이 없으면 예외 처리 ")
    @Test
    void getStatus_throwsTest() {
        //given
        given(tokenRepo.findByUserUuid("fakeUserUuid")).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> tokenService.getStatus("fakeUserUuid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토큰이 없습니다");
    }
}
