package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.service.StockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/movements")
public class MovementController {

    private StockService stockService;

    @PostMapping("/stock-in")
    public ResponseEntity<MovementResponseDto> stockIn(@RequestBody @Valid MovementInDto movementDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.stockIn(movementDto));
    }

    @PostMapping("/stock-out")
    public ResponseEntity<MovementResponseDto> stockOut(@RequestBody @Valid MovementOutDto movementDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.stockOut(movementDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDetailDto> searchMovement(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(stockService.searchMovement(id));
    }

    @GetMapping()
    public ResponseEntity<Page<MovementSummaryDto>> findAll(
           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(stockService.findAll(pageable));
    }

    // Depois fazer deleteMovement e updateMovement.
}