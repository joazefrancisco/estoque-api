package com.joaze.estoqueapi.dto;

import java.time.LocalDateTime;

public record ProductResponseDto(

        Long id,
        String name,
        String description,
        LocalDateTime createAt
) {}
