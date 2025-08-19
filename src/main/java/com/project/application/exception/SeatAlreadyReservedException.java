package com.project.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SeatAlreadyReservedException extends RuntimeException {
    public SeatAlreadyReservedException(String msg) {
        super(msg);
    }
}
