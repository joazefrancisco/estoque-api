package com.joaze.estoqueapi.dto.movement;

import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementSummaryDto(
        Long id,
        Integer quantity,
        BigDecimal unitCost,
        BigDecimal valueTotal,
        LocalDateTime date,
        Product productId
) {
}
