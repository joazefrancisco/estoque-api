package com.joaze.estoqueapi.dto.movement;

import java.math.BigDecimal;

public record MovementResponseDto(
        Long movementId,
        Long productId,
        Integer quantity,
        BigDecimal unitCost,
        BigDecimal totalValue,
        Integer remainingQuantity
) {}
