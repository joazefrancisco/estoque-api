package com.joaze.estoqueapi.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String description
) {}
