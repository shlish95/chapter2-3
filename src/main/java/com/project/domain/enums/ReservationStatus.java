package com.project.domain.enums;

public enum ReservationStatus {
    HOLD("임시 보류"),
    CONFIRMED("예약 확정"),
    CANCELLED("취소"),
    EXPIRED("만료");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return this == HOLD || this == CONFIRMED;
    }

    public String getDescription() {
        return description;
    }
}
