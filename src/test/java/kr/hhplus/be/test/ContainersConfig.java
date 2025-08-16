package kr.hhplus.be.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 모든 통합테스트가 상속해서 쓰는 컨테이너 베이스.
 * - MySQL: @ServiceConnection 으로 spring.datasource.* 자동 주입
 * - Redis: GenericContainer + @DynamicPropertySource 로 host/port 주입
 */
@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ContainersConfig {


}
