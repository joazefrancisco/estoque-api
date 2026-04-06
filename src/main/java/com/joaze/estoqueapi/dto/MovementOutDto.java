package com.joaze.estoqueapi.dto;

import java.math.BigDecimal;

public record MovementOutDto(

        Long productId,
        Integer quantity
) {}
