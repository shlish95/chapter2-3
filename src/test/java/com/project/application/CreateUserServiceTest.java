package com.project.application;

import com.project.domain.entity.Users;
import com.project.interfaces.UserRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    UserRepositoryInterface userRepo;

    @InjectMocks
    CreateUserService createUserService;

    @DisplayName("유저 생성 시 repository.save가 호출되고, 생성된 Users를 반환")
    @Test
    void createUser() {
        //given
        String name = "testName";
        String password = "testPassword";
        Users user = new Users(1L, name, password, BigDecimal.ZERO, LocalDateTime.now());

        given(userRepo.save(any(Users.class))).willReturn(user);

        //when
        Users result = createUserService.create(name, password);

        //then
        then(userRepo).should().save(any(Users.class));
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(name);


    }
}