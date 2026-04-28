package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.exception.ProductHasMovementsException;
import com.joaze.estoqueapi.model.ProductStatus;
import com.joaze.estoqueapi.service.ProductService;
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
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductSummaryDto> createProduct(@RequestBody @Valid ProductRequestDto productDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }

    @GetMapping
    public ResponseEntity<Page<ProductSummaryDto>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> searchProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productService.searchProduct(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSummaryDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto productDto){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id ,productDto));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> activeProduct(@PathVariable Long id) {
        productService.changeStatus(id, ProductStatus.ACTIVE);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Void> inactiveProduct(@PathVariable Long id) {
        productService.changeStatus(id, ProductStatus.INACTIVE);
        return ResponseEntity.noContent().build();
    }
}