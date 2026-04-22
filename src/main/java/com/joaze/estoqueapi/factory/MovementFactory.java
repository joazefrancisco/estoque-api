package com.joaze.estoqueapi.factory;

import com.joaze.estoqueapi.dto.stock.MovementInDto;
import com.joaze.estoqueapi.dto.stock.MovementOutDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementStatus;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MovementFactory {

    public Movement createIn(MovementInDto dto, Product product, BigDecimal valueTotal, Movement movementReference){
        Movement movement = new Movement();
        movement.setType(MovementType.IN);
        movement.setStatus(MovementStatus.ACTIVE);
        movement.setQuantity(dto.quantity());
        movement.setUnitCost(dto.unitCost());
        movement.setTotalValue(valueTotal);
        movement.setProduct(product);
        movement.setMovementReference(movementReference);
        return movement;
    }

    public Movement createOut(MovementOutDto dto, Product product, BigDecimal valueTotal, Movement movementReference){
        Movement movement = new Movement();
        movement.setType(MovementType.OUT);
        movement.setStatus(MovementStatus.ACTIVE);
        movement.setQuantity(dto.quantity());
        movement.setUnitCost(product.getAverageCost());
        movement.setTotalValue(valueTotal);
        movement.setProduct(product);
        movement.setMovementReference(movementReference);
        return movement;
    }
}
