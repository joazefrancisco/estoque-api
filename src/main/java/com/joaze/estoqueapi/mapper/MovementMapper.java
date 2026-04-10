package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.model.Movement;
import org.springframework.stereotype.Component;


@Component
public class MovementMapper {

    public Movement toEntityIn(MovementInDto movementInDto){
        Movement movement = new Movement();
        movement.setQuantity(movementInDto.quantity());
        movement.setUnitCost(movementInDto.unitCost());
        return movement;
    }

    public Movement toEntityOut(MovementOutDto movementOutDto){
        Movement movement = new Movement();
        movement.setQuantity(movementOutDto.quantity());
        return movement;
    }

    public MovementResponseDto toMovementResponseDto(Movement movement) {
        return new MovementResponseDto(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getQuantity(),
                movement.getUnitCost(),
                movement.getValueTotal(),
                movement.getProduct().getQuantity()
        );
    }

    public MovementSummaryDto toMovementSummaryDto(Movement movement){
        return new MovementSummaryDto(
                movement.getId(),
                movement.getType(),
                movement.getQuantity(),
                movement.getValueTotal(),
                movement.getDate(),
                movement.getProduct().getId()
        );
    }

    public MovementDetailDto toMovementDetailDto(Movement movement){
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
