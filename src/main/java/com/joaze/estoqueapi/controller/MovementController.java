package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.service.MovementService;
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

    private final MovementService movementService;

    @GetMapping("/{id}")
    public ResponseEntity<MovementDetailDto> searchMovement(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(movementService.searchMovement(id));
    }

    @GetMapping()
    public ResponseEntity<Page<MovementSummaryDto>> findAll(
           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(movementService.findAll(pageable));
    }

    @PutMapping("/{id}correct")
    public ResponseEntity<MovementUpdateResponseDto> toCorrectMovement(Long id, @RequestBody @Valid CorrectedRequestDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(movementService.toCorrectMovementIn(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelMovement(Long id){
        movementService.cancelMovement(id);
        return ResponseEntity.noContent().build();
    }
}