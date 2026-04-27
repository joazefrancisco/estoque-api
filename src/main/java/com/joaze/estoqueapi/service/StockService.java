package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.dto.stock.MovementInDto;
import com.joaze.estoqueapi.dto.stock.MovementOutDto;
import com.joaze.estoqueapi.exception.InsufficientStockException;
import com.joaze.estoqueapi.exception.ResourceNotFoundException;
import com.joaze.estoqueapi.factory.MovementFactory;
import com.joaze.estoqueapi.mapper.MovementMapper;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.MovementRepository;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class StockService {

    private final MovementRepository movementRepository;

    private final ProductRepository productRepository;

    private final MovementMapper movementMapper;

    private final MovementFactory movementFactory;

    private final StockCalculatorService stockCalculatorService;

    @Transactional
    public MovementResponseDto stockIn(MovementInDto dto) {
        Product productData = this.findProductOrThrow(dto.productId());

        BigDecimal valueTotalIn = stockCalculatorService.calculateValueTotal(dto.quantity(), dto.unitCost());
        stockCalculatorService.calculateStock(MovementType.IN, dto.quantity(), valueTotalIn, productData);

        Movement movement = movementFactory.createIn(dto, productData, valueTotalIn, null);

        movementRepository.save(movement);
        return movementMapper.toResponseDto(movement);
    }

    @Transactional
    public MovementResponseDto stockOut(MovementOutDto dto) {
        Product productData = this.findProductOrThrow(dto.productId());

        if (dto.quantity() > productData.getQuantity())
            throw new InsufficientStockException("Insufficient stock!");

        BigDecimal valueTotalOut = stockCalculatorService.calculateValueTotal(dto.quantity(), productData.getAverageCost());
        stockCalculatorService.calculateStock(MovementType.OUT, dto.quantity(), valueTotalOut, productData);
        Movement movement = movementFactory.createOut(dto, productData, valueTotalOut, null);

        if (productData.getQuantity() == 0) {
            movement.setTotalValue(productData.getTotalValue());
        }

        movementRepository.save(movement);
        return movementMapper.toResponseDto(movement);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}