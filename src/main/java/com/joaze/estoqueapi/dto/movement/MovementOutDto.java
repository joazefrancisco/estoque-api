package com.joaze.estoqueapi.dto.movement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record MovementOutDto(

        @NotNull
        Long productId,

        @NotNull
        @Min(1)
        Integer quantity
) {}
