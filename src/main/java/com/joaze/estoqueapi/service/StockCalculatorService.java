package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.CorrectedRequestDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class StockCalculatorService {

    public BigDecimal calculateValueTotal(Integer quantity, BigDecimal unitCost){
        if (quantity == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(quantity).multiply(unitCost);
    }

    public BigDecimal calculateAverageCost(BigDecimal totalValue, Integer totalQuantity){
        if (totalQuantity == 0) return BigDecimal.ZERO;
        return totalValue.divide(BigDecimal.valueOf(totalQuantity), 4, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOutTotal(Product product, Integer quantityOut){
        if (quantityOut == 0) return BigDecimal.ZERO;
        BigDecimal unitRealCost = product.getTotalValue()
                .divide(BigDecimal.valueOf(product.getQuantity()), 10, RoundingMode.HALF_UP);

        return unitRealCost.multiply(BigDecimal.valueOf(quantityOut));
    }

    public void balanceAdjustment(CorrectedRequestDto dto, Movement movement,  Product product){
        BigDecimal correctValueTotal = this.calculateValueTotal(dto.quantity(), dto.unitCost());

        Integer quantityAdjustment = product.getQuantity() + (dto.quantity() - movement.getQuantity());
        BigDecimal valueTotalAdjustment = product.getTotalValue().add(correctValueTotal.subtract(movement.getTotalValue()));
        BigDecimal averageCostAdjustment = this.calculateAverageCost(valueTotalAdjustment , quantityAdjustment);

        product.setQuantity(quantityAdjustment);
        product.setTotalValue(valueTotalAdjustment);
        product.setAverageCost(averageCostAdjustment);
    }
}
