package com.joaze.estoqueapi.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailDto(
        Long id,
        String name,
        String description,
        Integer quantity,
        BigDecimal averageCost,
        BigDecimal totalValue,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {}
