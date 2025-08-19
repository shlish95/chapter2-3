package com.project.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record HoldSeatRequest(
        Long userId,
        @JsonFormat(pattern = "yyyy-MM-dd")LocalDate date,
        int seatNum
        ) {}
