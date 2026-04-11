package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.*;
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
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class StockService {

    private MovementRepository movementRepository;

    private ProductRepository productRepository;

    private MovementMapper movementMapper;

    @Transactional
    public MovementResponseDto stockIn(MovementInDto movementDto){
        Product productData = this.findProductOrThrow(movementDto.productId());

        BigDecimal quantityIn = BigDecimal.valueOf(movementDto.quantity());
        BigDecimal valueTotalIn = quantityIn.multiply(movementDto.unitCost());

        productData.setTotalValue(productData.getTotalValue().add(valueTotalIn));
        productData.setQuantity(productData.getQuantity() + movementDto.quantity());
        productData.setAverageCost(productData.getTotalValue().divide(BigDecimal.valueOf(productData.getQuantity()), 4, RoundingMode.HALF_UP));

        Movement movement = movementMapper.toEntityIn(valueTotalIn, movementDto, productData);
        movementRepository.save(movement);
        return movementMapper.toResponseDto(movement);
    }

    @Transactional
    public MovementResponseDto stockOut(MovementOutDto movementDto) {
        Product productData = this.findProductOrThrow(movementDto.productId());

        if (movementDto.quantity() > productData.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock!");
        }

        BigDecimal valueTotalOut = BigDecimal.valueOf(movementDto.quantity()).multiply(productData.getAverageCost());
        Movement movement = movementMapper.toEntityOut(valueTotalOut, productData.getAverageCost(), movementDto, productData);
        boolean isLastStockMovement = productData.getQuantity().equals(movementDto.quantity());

        if (isLastStockMovement){
            movement.setTotalValue(productData.getTotalValue());
            productData.setTotalValue(BigDecimal.ZERO);
            productData.setAverageCost(BigDecimal.ZERO);
        } else {
            productData.setTotalValue(productData.getTotalValue().subtract(valueTotalOut));
        }

        productData.setQuantity(productData.getQuantity() - movementDto.quantity());

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