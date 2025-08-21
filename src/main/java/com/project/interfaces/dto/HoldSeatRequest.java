package com.project.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HoldSeatRequest(
        @NotNull Long userId,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd")LocalDate date,
        @Min(1) @Max(50) int seatNum
        ) {}
