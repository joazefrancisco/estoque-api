package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.stock.MovementInDto;
import com.joaze.estoqueapi.dto.stock.MovementOutDto;
import com.joaze.estoqueapi.dto.movement.MovementResponseDto;
import com.joaze.estoqueapi.dto.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Stock", description = "Create stock entry and exit movements")
public class StockController {

    private final StockService stockService;

    @Operation(summary = "Create stock entry movement")
    @ApiResponse(responseCode = "201", description = "Movement in created")
    @ApiResponse(responseCode = "422", description = "Product inactive")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PostMapping("/in")
    public ResponseEntity<MovementResponseDto> createStockIn(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for stock entry movement",
                    required = true
            )
            @RequestBody @Valid MovementInDto request){

        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.stockIn(request));
    }

    @Operation(summary = "Create stock exit movement")
    @ApiResponse(responseCode = "201", description = "Movement out created")
    @ApiResponse(responseCode = "400", description = "Insufficient stock")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "422", description = "Product inactive")
    @PostMapping("/out")
    public ResponseEntity<MovementResponseDto> createStockOut(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for stock exit movement",
                    required = true
            )
            @RequestBody @Valid MovementOutDto request){

        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.stockOut(request));
    }
}