package com.joaze.estoqueapi.dto.movement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.model.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementSummaryDto(
        Long id,
        MovementType type,
        Integer quantity,
        BigDecimal totalValue,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime date,
        String productName
) {}