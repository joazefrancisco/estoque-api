package com.joaze.estoqueapi.dto.movement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record MovementOutDto(

        @NotNull(message = "Product ID must not be null")
        Long productId,

        @NotNull(message = "Quantity must not be null")
        @Positive(message = "Quantity must be at least 1")
        Integer quantity
) {}
