package com.project.interfaces.repository;

public interface SeatHoldRepository {
    boolean tryHold(String date, int seatNum, String userUuid, long ttlSeconds);
    boolean releaseIfOwner(String date, int seatNum, String userUuid);
    String currentHolder(String date, int seatNum);
}
