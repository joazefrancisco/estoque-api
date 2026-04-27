package com.joaze.estoqueapi.dto.movement;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CorrectedOutDto(
        @NotNull(message = "Quantity must not be null")
        @Positive(message = "Quantity must be at least 1")
        Integer quantity
) {}
