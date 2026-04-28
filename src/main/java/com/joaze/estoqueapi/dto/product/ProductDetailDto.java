package com.joaze.estoqueapi.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joaze.estoqueapi.model.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailDto(
        Long id,
        String name,
        String description,
        Integer quantity,
        BigDecimal averageCost,
        BigDecimal totalValue,
        ProductStatus status,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
