package com.joaze.estoqueapi.dto.movement;

import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.model.MovementType;

import java.math.BigDecimal;

public record MovementSummaryDto(
        Long id,
        MovementType type,
        Integer quantity,
        BigDecimal totalValue,
        ProductSummaryDto product
) {}