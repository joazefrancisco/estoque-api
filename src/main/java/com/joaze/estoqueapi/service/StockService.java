package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.factory.MovementFactory;
import com.joaze.estoqueapi.mapper.MovementMapper;
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

@Service
@AllArgsConstructor
public class StockService {

    private final MovementRepository movementRepository;

    private final ProductRepository productRepository;

    private final MovementMapper movementMapper;

    private final MovementFactory movementFactory;

    private final StockCalculatorService stockCalculatorService;

    @Transactional
    public MovementResponseDto stockIn(MovementInDto movementDto){
        Product productData = this.findProductOrThrow(movementDto.productId());

        BigDecimal valueTotalIn = stockCalculatorService.calculateEntryTotal(movementDto.quantity(), movementDto.unitCost());

        productData.setTotalValue(productData.getTotalValue().add(valueTotalIn));
        productData.setQuantity(productData.getQuantity() + movementDto.quantity());
        productData.setAverageCost(stockCalculatorService.calculateAverageCost(productData.getTotalValue(), productData.getQuantity()));

        Movement movement = movementFactory.createIn(movementDto, productData, valueTotalIn);

        movementRepository.save(movement);
        return movementMapper.toResponseDto(movement);
    }

    @Transactional
    public MovementResponseDto stockOut(MovementOutDto movementDto) {
        Product productData = this.findProductOrThrow(movementDto.productId());

        if (movementDto.quantity() > productData.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock!");
        }

        BigDecimal valueTotalOut = stockCalculatorService.calculateOutTotal(productData, movementDto.quantity());
        int newQuantity = productData.getQuantity() - movementDto.quantity();

        Movement movement = movementFactory.createOut(movementDto, productData, valueTotalOut);

        if (newQuantity == 0){
            movement.setTotalValue(productData.getTotalValue());
            productData.setTotalValue(BigDecimal.ZERO);
            productData.setAverageCost(BigDecimal.ZERO);
        } else {
            productData.setTotalValue(productData.getTotalValue().subtract(valueTotalOut));
        }

        productData.setQuantity(newQuantity);
        movementRepository.save(movement);
        return movementMapper.toResponseDto(movement);
    }

    public MovementDetailDto searchMovement(Long id){
        Movement movementData = movementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        return movementMapper.toDetailDto(movementData);
    }

    public Page<MovementSummaryDto> findAll(Pageable pageable){
        return movementRepository.findAll(pageable).map(movementMapper::toSummaryDto);
    }

    private Product findProductOrThrow(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}