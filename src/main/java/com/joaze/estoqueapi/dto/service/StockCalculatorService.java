package com.joaze.estoqueapi.dto.service;

import com.joaze.estoqueapi.dto.movement.CorrectedInDto;
import com.joaze.estoqueapi.dto.movement.CorrectedOutDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class StockCalculatorService {

    public BigDecimal calculateValueTotal(Integer quantity, BigDecimal unitCost){
        if (quantity == 0) return BigDecimal.ZERO;
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal calculateAverageCost(BigDecimal totalValue, Integer totalQuantity){
        if (totalQuantity == 0) return BigDecimal.ZERO;
        return totalValue.divide(BigDecimal.valueOf(totalQuantity), 4, RoundingMode.HALF_UP);
    }

    public void calculateStock(MovementType type, Integer quantity, BigDecimal valueTotal, Product product){

        if (MovementType.IN.equals(type)) {
            int newQuantity = product.getQuantity() + quantity;
            BigDecimal newValueTotal = product.getTotalValue().add(valueTotal);
            BigDecimal newAverageCost = this.calculateAverageCost(newValueTotal, newQuantity);

            this.calculateProduct(newQuantity, newAverageCost, newValueTotal, product);
        }

        if (MovementType.OUT.equals(type)) {
            int newQuantity = product.getQuantity() - quantity;

            if (newQuantity == 0){
                this.calculateProduct(0, BigDecimal.ZERO, BigDecimal.ZERO, product);

            } else {
                BigDecimal newValueTotal = product.getTotalValue().subtract(valueTotal);
                this.calculateProduct(newQuantity, product.getAverageCost(), newValueTotal, product);
            }
        }
    }

    public void balanceAdjustment(MovementType type , Integer quantity, BigDecimal valueTotal, Movement movement, Product product){

        if (MovementType.IN.equals(type)){
            int quantityAdjustment = product.getQuantity() + (quantity - movement.getQuantity());
            BigDecimal valueTotalAdjustment = product.getTotalValue().add(valueTotal.subtract(movement.getTotalValue()));
            BigDecimal averageCostAdjustment = this.calculateAverageCost(valueTotalAdjustment , quantityAdjustment);
            this.calculateProduct(quantityAdjustment, averageCostAdjustment, valueTotalAdjustment, product);
        }

        else if (MovementType.OUT.equals(type)){
            int newQuantity = movement.getQuantity() - quantity;

            if (newQuantity == 0){
                this.calculateProduct(0, BigDecimal.ZERO, BigDecimal.ZERO, product);

            } else {
                int quantityAdjustment = product.getQuantity() + quantity;
                BigDecimal valueTotalAdjustment = product.getTotalValue().add(movement.getTotalValue().subtract(valueTotal));
                BigDecimal averageCostAdjustment = this.calculateAverageCost(valueTotalAdjustment , quantityAdjustment);
                this.calculateProduct(quantityAdjustment, averageCostAdjustment, valueTotalAdjustment, product);
            }
        }
    }

    private void calculateProduct(Integer quantity, BigDecimal averageCost, BigDecimal valueTotal, Product product){
        product.setQuantity(quantity);
        product.setAverageCost(averageCost);
        product.setTotalValue(valueTotal);
    }
}
