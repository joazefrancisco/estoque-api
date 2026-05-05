package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.model.ProductStatus;
import com.joaze.estoqueapi.dto.service.ProductService;
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
@RequestMapping("/products")
@Tag(name = "Products", description = "Products operations ")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create product")
    @ApiResponse(responseCode = "201", description = "Product created")
    @PostMapping
    public ResponseEntity<ProductSummaryDto> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to create a product",
                    required = true
            )
            @RequestBody @Valid ProductRequestDto request){

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @Operation(summary = "Find all products with pagination")
    @ApiResponse(responseCode = "200", description = "Page of products returned")
    @GetMapping
    public ResponseEntity<Page<ProductSummaryDto>> findAll(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(pageable));
    }

    @Operation(summary = "Find product by ID")
    @ApiResponse(responseCode = "200", description = "Find by product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> searchProduct(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK).body(productService.searchProduct(id));
    }

    @Operation(summary = "Update product")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "422", description = "Product inactive")
    @ApiResponse(responseCode = "500", description = "Unexpected error")
    @PutMapping("/{id}")
    public ResponseEntity<ProductSummaryDto> updateProduct(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product data",
                    required = true
            )
            @RequestBody @Valid ProductRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id ,request));
    }

    @Operation(summary = "Active product")
    @ApiResponse(responseCode = "204", description = "Activated product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error")
    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> activeProduct(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id) {

        productService.changeStatus(id, ProductStatus.ACTIVE);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Inactive product")
    @ApiResponse(responseCode = "204", description = "Inactive product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error")
    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Void> inactiveProduct(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long id) {

        productService.changeStatus(id, ProductStatus.INACTIVE);
        return ResponseEntity.noContent().build();
    }
}