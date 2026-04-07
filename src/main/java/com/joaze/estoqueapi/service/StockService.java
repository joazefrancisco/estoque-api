package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.MovementInDto;
import com.joaze.estoqueapi.dto.movement.MovementOutDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.MovementRepository;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
// import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class StockService {

    private MovementRepository movementRepository;

    private ProductRepository productRepository;

    @Transactional
    public void stockIn(MovementInDto movementDto){
        Product productData = productRepository.findById(movementDto.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        BigDecimal quantity = BigDecimal.valueOf(movementDto.quantity());

        productData.setTotalValue(productData.getTotalValue().add(quantity.multiply(movementDto.unitCost())));
        productData.setQuantity(productData.getQuantity() + movementDto.quantity());  // Dps alterar para RoundingMode
        productData.setAverageCost(productData.getTotalValue().divide(BigDecimal.valueOf(productData.getQuantity()), 4, BigDecimal.ROUND_HALF_UP));
        productData.setUpdatedAt(LocalDateTime.now());

        Movement movement = this.createMovement(MovementType.ENTRADA, movementDto, productData);
        movementRepository.save(movement);
    }

    @Transactional
    public void stockOut(MovementOutDto movementDto) {
        Product productData = productRepository.findById(movementDto.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (movementDto.quantity() > productData.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock!");
        }

        BigDecimal movementValue = BigDecimal.valueOf(movementDto.quantity()).multiply(productData.getAverageCost());
        boolean isLastStockMovement = productData.getQuantity().equals(movementDto.quantity());

        Movement movement = new Movement();
        movement.setType(MovementType.SAIDA);
        movement.setQuantity(movementDto.quantity());
        movement.setUnitCost(productData.getAverageCost());
        movement.setDate(LocalDateTime.now());
        movement.setProduct(productData);

        if (isLastStockMovement){
            movement.setValueTotal(productData.getTotalValue());
            productData.setTotalValue(BigDecimal.ZERO);
            productData.setAverageCost(BigDecimal.ZERO);
        } else {
            movement.setValueTotal(movementValue);
            productData.setTotalValue(productData.getTotalValue().subtract(movementValue));
        }

        productData.setQuantity(productData.getQuantity() - movementDto.quantity());
        productData.setUpdatedAt(LocalDateTime.now());

        movementRepository.save(movement);
    }

    // Refatorar aqui
    private Movement createMovement(MovementType type, MovementInDto movementDto, Product product){
        Movement movement = new Movement();

        movement.setType(type);
        movement.setQuantity(movementDto.quantity());
        movement.setUnitCost(movementDto.unitCost());
        movement.setValueTotal(BigDecimal.valueOf(movementDto.quantity()).multiply(movementDto.unitCost()));
        movement.setDate(LocalDateTime.now());
        movement.setProduct(product);
        return movement;
    }
}