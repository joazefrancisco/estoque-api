package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.movement.MovementDetailDto;
import com.joaze.estoqueapi.dto.movement.MovementSummaryDto;
import com.joaze.estoqueapi.dto.movement.correctedRequestDto;
import com.joaze.estoqueapi.dto.movement.MovementUpdateResponseDto;
import com.joaze.estoqueapi.exception.ResourceNotFoundException;
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

    private final StockCalculatorService stockCalculatorService;

    public MovementDetailDto searchMovement(Long id) {
        Movement movementData = this.findMovementOrThrow(id);
        return movementMapper.toDetailDto(movementData);
    }

    public Page<MovementSummaryDto> findAll(Pageable pageable) {
        return movementRepository.findAll(pageable).map(movementMapper::toSummaryDto);
    }

    @Transactional
    public MovementUpdateResponseDto toCorrectMovement(Long id, correctedRequestDto dto){
        Movement movementData = this.findMovementOrThrow(id);
        movementData.setStatus(MovementStatus.CORRECTED);

        Product productData = movementData.getProduct();

        Integer newQuantityMovement = stockCalculatorService.recalculationOfQuantityInStock(
                dto.quantity(), movementData.getQuantity()
        );

        BigDecimal newValueTotalMovement = stockCalculatorService.recalculationOfValueTotalInStock(
                dto.quantity(), dto.unitCost(), movementData.getTotalValue()
        );

        if (MovementType.ENTRADA.equals(dto.type())){
            productData.setTotalValue(productData.getTotalValue().add(newValueTotalMovement));
            productData.setQuantity(productData.getQuantity() + newQuantityMovement);
            productData.setAverageCost(dto.unitCost());

        } else if (MovementType.SAIDA.equals(dto.type())){
        }
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
