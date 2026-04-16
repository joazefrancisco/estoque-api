package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.dto.resposne.ApiResponse;
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
    public ResponseEntity<ApiResponse<MovementResponseDto>> stockIn(@RequestBody @Valid MovementInDto movementDto){
        MovementResponseDto movement = stockService.stockIn(movementDto);
        ApiResponse<MovementResponseDto> response = new ApiResponse<>("Stock entry created successfully", movement);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/stock-out")
    public void stockOut(@RequestBody @Valid MovementOutDto movementDto){
        stockService.stockOut(movementDto);
    }

    @GetMapping("/{id}")
    public MovementDetailDto searchMovement(@PathVariable Long id){
        return stockService.searchMovement(id);
    }

    @GetMapping()
    public Page<MovementSummaryDto> findAll(
           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return stockService.findAll(pageable);
    }
}