package com.joaze.estoqueapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MovementDto (

        @NotNull
        Long productId,

        @NotNull
        @Min(1)
        Integer quantity
) {}
