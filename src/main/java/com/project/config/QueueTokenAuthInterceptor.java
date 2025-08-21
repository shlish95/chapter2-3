package com.project.config;

import com.project.application.QueueTokenQueryService;
import com.project.domain.entity.QueueToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QueueTokenAuthInterceptor implements HandlerInterceptor {

    private final QueueTokenQueryService queueTokenQueryService;
    private final QueueTokenAuthProperties queueTokenAuthProps;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        if (!queueTokenAuthProps.isEnabled()) {
            return true;
        }

        String uuid = req.getHeader("X-QUEUE-UUID");
        if (uuid == null || uuid.isBlank()) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "대기열 토큰이 필요합니다.");
            return false;
        }

        Optional<QueueToken> tokenOpt = queueTokenQueryService.get(uuid);
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            res.sendError(HttpStatus.FORBIDDEN.value(), "대기열 토큰이 유효하지 않습니다.");
            return false;
        }

        return true;
    }
}
