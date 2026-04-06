package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.MovementInDto;
import com.joaze.estoqueapi.dto.MovementOutDto;
import com.joaze.estoqueapi.service.ProductStockService;
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

    private ProductStockService productStockService;

    @PostMapping("/stock-in")
    public void stockIn(@RequestBody @Valid MovementInDto movementDto){
        productStockService.stockIn(movementDto);
    }

    @PostMapping("/stock-out")
    public void stockOut(@RequestBody @Valid MovementOutDto movementDto){
        productStockService.stockOut(movementDto);
    }
}