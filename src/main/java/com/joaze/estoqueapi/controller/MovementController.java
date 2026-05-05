package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Movements", description = "Stock movements operations")
public class MovementController {

    private final MovementService movementService;

    @Operation(summary = "Find movement by id")
    @ApiResponse(responseCode = "200", description = "Movement found")
    @ApiResponse(responseCode = "404", description = "Movement not found")
    @GetMapping("/{id}")
    public ResponseEntity<MovementDetailDto> searchMovement(
            @Parameter(description = "Movement ID", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(movementService.searchMovement(id));
    }

    @Operation(summary = "Find all movements with pagination")
    @ApiResponse(responseCode = "200", description = "Page of movements returned")
    @GetMapping()
    public ResponseEntity<Page<MovementSummaryDto>> findAll(
           @ParameterObject
           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
           Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(movementService.findAll(pageable));
    }

    @Operation(summary = "Correct in movement and recalculate stock")
    @ApiResponse(responseCode = "200", description = "Movement of in corrected")
    @ApiResponse(responseCode = "400", description = "Insufficient stock")
    @ApiResponse(responseCode = "404", description = "Movement not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error")
    @PatchMapping("/{id}/correct-in")
    public ResponseEntity<MovementResponseDto> toCorrectMovementIn(
            @Parameter(description = "Movement ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Date for correcting an OUT movement",
                    required = true
            )
            @RequestBody @Valid CorrectedInDto request) {

        return ResponseEntity.status(HttpStatus.OK).body(movementService.toCorrectMovementIn(id, request));
    }

    @Operation(summary = "Correct out movement and recalculate stock")
    @ApiResponse(responseCode = "200", description = "Movement corrected")
    @ApiResponse(responseCode = "400", description = "Insufficient stock")
    @ApiResponse(responseCode = "404", description = "Movement not found")
    @ApiResponse(responseCode = "422", description = "Product inactive")
    @ApiResponse(responseCode = "500", description = "Unexpected error")
    @PatchMapping("/{id}/correct-out")
    public ResponseEntity<MovementResponseDto> toCorrectMovementOut(
            @Parameter(description = "Movement ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for correcting an IN movement",
                    required = true
            )
            @RequestBody @Valid CorrectedOutDto request) {

        return ResponseEntity.status(HttpStatus.OK).body(movementService.toCorrectMovementOut(id, request));
    }

    @Operation(description = "Cancel movement")
    @ApiResponse(responseCode = "204", description = "Movement canceled")
    @ApiResponse(responseCode = "404", description = "Movement not found")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelMovement(
            @Parameter(description = "Movement ID", example = "1")
            @PathVariable Long id){

        movementService.cancelMovement(id);
        return ResponseEntity.noContent().build();
    }
}