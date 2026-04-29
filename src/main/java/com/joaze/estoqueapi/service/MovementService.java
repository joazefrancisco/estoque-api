package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.dto.stock.MovementInDto;
import com.joaze.estoqueapi.dto.stock.MovementOutDto;
import com.joaze.estoqueapi.exception.BusinessException;
import com.joaze.estoqueapi.exception.InsufficientStockException;
import com.joaze.estoqueapi.exception.ResourceNotFoundException;
import com.joaze.estoqueapi.factory.MovementFactory;
import com.joaze.estoqueapi.mapper.MovementMapper;
import com.joaze.estoqueapi.model.*;
import com.joaze.estoqueapi.repository.MovementRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MovementService {

    private final MovementRepository movementRepository;

    private final MovementMapper movementMapper;

    private final MovementFactory movementFactory;

    private final StockCalculatorService stockCalculatorService;

    public MovementDetailDto searchMovement(Long id) {
        Movement movementData = this.findMovementOrThrow(id);
        return movementMapper.toDetailDto(movementData);
    }

    public Page<MovementSummaryDto> findAll(Pageable pageable) {
        return movementRepository
               .findByStatus(MovementStatus.ACTIVE, pageable)
               .map(movementMapper::toSummaryDto);
    }

    @Transactional
    public MovementResponseDto toCorrectMovementIn(Long id, CorrectedInDto dto) {
        Movement movementData = this.findMovementOrThrow(id);

        if (MovementStatus.CORRECTED.equals(movementData.getStatus()))
            throw new BusinessException("The movement has already been corrected");

        Product productData = movementData.getProduct();

        if (ProductStatus.INACTIVE.equals(productData.getStatus()))
            throw new BusinessException("Product inactive");

        movementData.setStatus(MovementStatus.CORRECTED);
        BigDecimal valueTotalAdjustment = stockCalculatorService.calculateValueTotal(dto.quantity() ,dto.unitCost());
        stockCalculatorService.balanceAdjustment(MovementType.IN, dto.quantity(), valueTotalAdjustment, movementData, productData);

        MovementInDto movementInDto = movementMapper.fromCorrectionIn(dto);
        Movement correctMovement = movementFactory.createIn(movementInDto, productData, valueTotalAdjustment, movementData);

        movementRepository.save(correctMovement);
        return movementMapper.toResponseDto(correctMovement);
    }

    @Transactional
    public MovementResponseDto toCorrectMovementOut(Long id, CorrectedOutDto dto) {
        Movement movementData = this.findMovementOrThrow(id);

        if (MovementStatus.CORRECTED.equals(movementData.getStatus()))
            throw new BusinessException("The movement has already been corrected");

        Product productData = movementData.getProduct();

        if (ProductStatus.INACTIVE.equals(productData.getStatus()))
            throw new BusinessException("Product inactive");

        if (dto.quantity() > productData.getQuantity())
            throw new InsufficientStockException("Insufficient stock!");

        movementData.setStatus(MovementStatus.CORRECTED);
        BigDecimal valueTotalAdjustment = stockCalculatorService.calculateValueTotal(dto.quantity() , movementData.getUnitCost());
        stockCalculatorService.balanceAdjustment(MovementType.OUT, dto.quantity(), valueTotalAdjustment, movementData, productData);

        MovementOutDto movementOutDto = movementMapper.fromCorrectionOut(dto);
        Movement correctMovement = movementFactory.createOut(movementOutDto, productData, valueTotalAdjustment, movementData);

        movementRepository.save(correctMovement);
        return movementMapper.toResponseDto(correctMovement);
    }

    @Transactional
    public void cancelMovement(Long id){
        Movement movementData = this.findMovementOrThrow(id);
        Product productData = movementData.getProduct();

        if (ProductStatus.INACTIVE.equals(productData.getStatus()))
            throw new BusinessException("Product inactive");
    }

    private Movement findMovementOrThrow(Long id){
        return movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));
    }
}
