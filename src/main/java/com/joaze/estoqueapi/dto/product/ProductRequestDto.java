package com.joaze.estoqueapi.dto.product;

import jakarta.validation.constraints.NotBlank;

public record ProductRequestDto(

        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Description must not be blank")
        String description
) {}
