package com.joaze.estoqueapi.dto.product;

import java.time.LocalDateTime;

public record ProductSummaryDto(

        Long id,
        String name,
        String description,
        LocalDateTime createAt
) {}
