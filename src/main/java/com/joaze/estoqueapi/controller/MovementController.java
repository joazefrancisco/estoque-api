package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.movement.MovementInDto;
import com.joaze.estoqueapi.dto.movement.MovementOutDto;
import com.joaze.estoqueapi.dto.movement.MovementDetailDto;
import com.joaze.estoqueapi.service.StockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public MovementDetailDto searchMovement(@PathVariable Long id){
        return stockService.searchMovement(id);
    }

    @GetMapping()
    public Page<MovementDetailDto> findAll(
           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return stockService.findAll(pageable);
    }
}