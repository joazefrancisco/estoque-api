package com.joaze.estoqueapi.dto.product;

import java.time.LocalDateTime;

public record ProductResponseDto(

        Long id,
        String name,
        String description,
        LocalDateTime createAt
) {}
