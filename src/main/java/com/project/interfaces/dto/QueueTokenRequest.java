package com.project.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record QueueTokenRequest(@NotBlank String userUuid) {}
