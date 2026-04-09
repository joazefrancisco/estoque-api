package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.MovementInDto;
import com.joaze.estoqueapi.dto.movement.MovementOutDto;
import com.joaze.estoqueapi.dto.movement.MovementDetailDto;
import com.joaze.estoqueapi.dto.movement.MovementSummaryDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.MovementRepository;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class StockService {

    private MovementRepository movementRepository;

    private ProductRepository productRepository;

    @Transactional
    public void stockIn(MovementInDto movementDto){
        Product productData = this.findProductOrThrow(movementDto.productId());

        BigDecimal quantityIn = BigDecimal.valueOf(movementDto.quantity());
        BigDecimal valueTotalIn = quantityIn.multiply(movementDto.unitCost());

        productData.setTotalValue(productData.getTotalValue().add(valueTotalIn));
        productData.setQuantity(productData.getQuantity() + movementDto.quantity());
        productData.setAverageCost(productData.getTotalValue().divide(BigDecimal.valueOf(productData.getQuantity()), 4, RoundingMode.HALF_UP));
        productData.setUpdatedAt(LocalDateTime.now());

        Movement movement = this.createMovement(MovementType.ENTRADA, movementDto.quantity(), productData);
        movement.setValueTotal(valueTotalIn);
        movement.setUnitCost(movementDto.unitCost());
        movementRepository.save(movement);
    }

    @Transactional
    public void stockOut(MovementOutDto movementDto) {
        Product productData = this.findProductOrThrow(movementDto.productId());

        if (movementDto.quantity() > productData.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock!");
        }

        Movement movement = this.createMovement(MovementType.SAIDA, movementDto.quantity(), productData);
        movement.setUnitCost(productData.getAverageCost());
        movement.setProduct(productData);

        BigDecimal movementValue = BigDecimal.valueOf(movementDto.quantity()).multiply(productData.getAverageCost());
        boolean isLastStockMovement = productData.getQuantity().equals(movementDto.quantity());

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

    public MovementDetailDto searchMovement(Long id){
        Movement movementData = movementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        return new MovementDetailDto(
                movementData.getId(),
                movementData.getType(),
                movementData.getQuantity(),
                movementData.getUnitCost(),
                movementData.getValueTotal().setScale(2, RoundingMode.HALF_UP),
                movementData.getDate(),
                movementData.getProduct().getId()
        );
    }

    public Page<MovementSummaryDto> findAll(Pageable pageable){
        return movementRepository.findAll(pageable).map(movementData -> new MovementSummaryDto(
                movementData.getId(),
                movementData.getType(),
                movementData.getQuantity(),
                movementData.getValueTotal().setScale(2, RoundingMode.HALF_UP),
                movementData.getDate(),
                movementData.getProduct().getId()
        ));
    }


    // Private function
    private Product findProductOrThrow(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private Movement createMovement(MovementType type,  Integer quantity, Product product){
        Movement movement = new Movement();
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setDate(LocalDateTime.now());
        movement.setProduct(product);
        return movement;
    }
}