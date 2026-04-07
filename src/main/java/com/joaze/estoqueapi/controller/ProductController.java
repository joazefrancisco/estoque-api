package com.joaze.estoqueapi.controller;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductResponseDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody @Valid ProductRequestDto productDto){
        return productService.createProduct(productDto);
    }

    @GetMapping
    public Page<ProductResponseDto> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable){
        return productService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ProductDetailDto searchProduct(@PathVariable Long id){
        return productService.searchProduct(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id,@RequestBody @Valid ProductRequestDto productDto){
        return productService.updateProduct(id ,productDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}