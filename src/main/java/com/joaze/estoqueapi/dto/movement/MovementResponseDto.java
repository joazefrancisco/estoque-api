package com.joaze.estoqueapi.dto.movement;

import java.math.BigDecimal;

public record MovementResponseDto(
        boolean success,
        String message,
        Long movementId,
        Long productId,
        Integer quantity,
        BigDecimal unitCost,
        BigDecimal valueTotal,
        Integer remainingQuantity
) {}
