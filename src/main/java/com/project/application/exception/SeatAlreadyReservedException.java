package com.project.application.exception;

public class SeatAlreadyReservedException extends RuntimeException {
    public SeatAlreadyReservedException(String msg) {
        super(msg);
    }
}
