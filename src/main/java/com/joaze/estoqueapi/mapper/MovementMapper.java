package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class MovementMapper {

    public Movement toEntityIn(BigDecimal movementTotalValue, MovementInDto movementInDto, Product product){
        Movement movement = new Movement();
        movement.setType(MovementType.ENTRADA);
        movement.setQuantity(movementInDto.quantity());
        movement.setUnitCost(movementInDto.unitCost());
        movement.setTotalValue(movementTotalValue);
        movement.setProduct(product);
        return movement;
    }

    public Movement toEntityOut(BigDecimal movementTotalValue, BigDecimal averageCost, MovementOutDto movementOutDto, Product product){
        Movement movement = new Movement();
        movement.setType(MovementType.SAIDA);
        movement.setQuantity(movementOutDto.quantity());
        movement.setUnitCost(averageCost);
        movement.setTotalValue(movementTotalValue);
        movement.setProduct(product);
        return movement;
    }

    public MovementResponseDto toResponseDto(Movement movement) {
        return new MovementResponseDto(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getQuantity(),
                movement.getUnitCost(),
                movement.getTotalValue(),
                movement.getProduct().getQuantity()
        );
    }

    public MovementSummaryDto toSummaryDto(Movement movement){
        return new MovementSummaryDto(
                movement.getId(),
                movement.getType(),
                movement.getQuantity(),
                movement.getTotalValue(),
                movement.getDate(),
                movement.getProduct().getId()
        );
    }

    public MovementDetailDto toDetailDto(Movement movement){
        return new MovementDetailDto(
                movement.getId(),
                movement.getType(),
                movement.getQuantity(),
                movement.getUnitCost(),
                movement.getTotalValue(),
                movement.getDate(),
                movement.getProduct().getId()
        );
    }
}
