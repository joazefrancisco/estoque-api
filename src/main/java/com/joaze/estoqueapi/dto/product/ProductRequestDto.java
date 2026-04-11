package com.joaze.estoqueapi.dto.product;

import jakarta.validation.constraints.NotBlank;

public record ProductRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String description
) {}
