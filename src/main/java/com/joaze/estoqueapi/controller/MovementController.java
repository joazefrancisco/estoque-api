package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.movement.MovementInDto;
import com.joaze.estoqueapi.dto.movement.MovementOutDto;
import com.joaze.estoqueapi.service.StockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/movements")
public class MovementController {

    private StockService stockService;

    @PostMapping("/stock-in")
    public void stockIn(@RequestBody @Valid MovementInDto movementDto){
        stockService.stockIn(movementDto);
    }

    @PostMapping("/stock-out")
    public void stockOut(@RequestBody @Valid MovementOutDto movementDto){
        stockService.stockOut(movementDto);
    }
}