package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.MovementDto;
import com.joaze.estoqueapi.service.ProductStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(name = "/movements")
public class MovementController {

    private ProductStockService productStockService;

    @PostMapping("/stock-in")
    public void stockIn(@RequestBody @Valid MovementDto movementDto){
        productStockService.stockIn(movementDto);
    }

    @PostMapping("/stock-out")
    public void stockOut(@RequestBody @Valid MovementDto movementDto){
        productStockService.stockOut(movementDto);
    }
}