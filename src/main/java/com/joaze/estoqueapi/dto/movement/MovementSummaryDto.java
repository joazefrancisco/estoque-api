package com.joaze.estoqueapi.dto.movement;

import com.joaze.estoqueapi.model.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementSummaryDto(
        Long id,
        MovementType type,
        Integer quantity,
        BigDecimal totalValue,
        LocalDateTime date,
        Long productId
) {
}
