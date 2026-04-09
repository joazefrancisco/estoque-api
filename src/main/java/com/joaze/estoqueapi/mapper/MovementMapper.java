package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
public class MovementMapper {

    public static Movement toEntityIn(MovementInDto movementInDto){
        Movement movement = new Movement();
        movement.setQuantity(movementInDto.quantity());
        movement.setUnitCost(movementInDto.unitCost());
        return movement;
    }

    public static Movement toEntityOut(MovementOutDto movementOutDto){
        Movement movement = new Movement();
        movement.setQuantity(movementOutDto.quantity());
        return movement;
    }

    public static MovementResponseDto toMovementResponseDto(Movement movement) {
        return new MovementResponseDto(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getQuantity(),
                movement.getUnitCost(),
                movement.getValueTotal(),
                movement.getProduct().getQuantity()
        );
    }

    public static MovementSummaryDto toMovementSummaryDto(Movement movement){
        return new MovementSummaryDto(
                movement.getId(),
                movement.getType(),
                movement.getQuantity(),
                movement.getValueTotal(),
                movement.getDate(),
                movement.getProduct().getId()
        );
    }

    public static MovementDetailDto toMovementDetailDto(Movement movement){
        return new MovementDetailDto(
                movement.getId(),
                movement.getType(),
                movement.getQuantity(),
                movement.getUnitCost(),
                movement.getValueTotal(),
                movement.getDate(),
                movement.getProduct().getId()
        );
    }
}
