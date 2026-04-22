package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.MovementDetailDto;
import com.joaze.estoqueapi.dto.movement.MovementSummaryDto;
import com.joaze.estoqueapi.dto.movement.CorrectedRequestDto;
import com.joaze.estoqueapi.dto.movement.MovementUpdateResponseDto;
import com.joaze.estoqueapi.dto.stock.MovementInDto;
import com.joaze.estoqueapi.exception.ResourceNotFoundException;
import com.joaze.estoqueapi.factory.MovementFactory;
import com.joaze.estoqueapi.mapper.MovementMapper;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementStatus;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
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
        return movementRepository.findAll(pageable).map(movementMapper::toSummaryDto);
    }

    @Transactional
    public MovementUpdateResponseDto toCorrectMovementIn(Long id, CorrectedRequestDto dto){
        Movement movementData = this.findMovementOrThrow(id);
        movementData.setStatus(MovementStatus.CORRECTED);
        Product productData = movementData.getProduct();

        stockCalculatorService.balanceAdjustment(dto, movementData, productData);

        BigDecimal valueTotalAdjustment = stockCalculatorService.calculateValueTotal(dto.quantity() ,dto.unitCost());
        Movement correctMovement = movementFactory.createIn(dto, productData, valueTotalAdjustment, movementData);

        movementRepository.save(correctMovement);
        return null;
    }

    public void cancelMovement(Long id){
        this.findMovementOrThrow(id);
    }

    private Movement findMovementOrThrow(Long id){
        return movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));
    }
}
