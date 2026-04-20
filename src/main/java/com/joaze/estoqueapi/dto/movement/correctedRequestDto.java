package com.joaze.estoqueapi.dto.movement;

import com.joaze.estoqueapi.model.MovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record correctedRequestDto(
        @NotBlank(message = "Type must not be blank")
        MovementType type,

        @NotNull(message = "Quantity must not be null")
        @Positive(message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Unit cost must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit cost must be greater than 0")
        BigDecimal unitCost
) {}
