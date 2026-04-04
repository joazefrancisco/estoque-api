package com.joaze.estoqueapi.dto;

import java.math.BigDecimal;

public record ProductResponseDto(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity
) {}
