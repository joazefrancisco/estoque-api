package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.stock.MovementInDto;
import com.joaze.estoqueapi.dto.stock.MovementOutDto;
import com.joaze.estoqueapi.dto.movement.MovementResponseDto;
import com.joaze.estoqueapi.service.StockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    @PostMapping("/in")
    public ResponseEntity<MovementResponseDto> stockIn(@RequestBody @Valid MovementInDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.stockIn(dto));
    }

    @PostMapping("/out")
    public ResponseEntity<MovementResponseDto> stockOut(@RequestBody @Valid MovementOutDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.stockOut(dto));
    }
}