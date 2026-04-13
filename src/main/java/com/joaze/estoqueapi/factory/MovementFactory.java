package com.joaze.estoqueapi.factory;

import com.joaze.estoqueapi.dto.movement.MovementInDto;
import com.joaze.estoqueapi.dto.movement.MovementOutDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MovementFactory {

    public Movement createIn(MovementInDto dto, Product product, BigDecimal valueTotal){
        Movement movement = new Movement();
        movement.setType(MovementType.ENTRADA);
        movement.setQuantity(dto.quantity());
        movement.setUnitCost(dto.unitCost());
        movement.setTotalValue(valueTotal);
        movement.setProduct(product);
        return movement;
    }

    public Movement createOut(MovementOutDto dto, Product product, BigDecimal valueTotal){
        Movement movement = new Movement();
        movement.setType(MovementType.SAIDA);
        movement.setQuantity(dto.quantity());
        movement.setUnitCost(product.getAverageCost());
        movement.setTotalValue(valueTotal);
        movement.setProduct(product);
        return movement;
    }
}
