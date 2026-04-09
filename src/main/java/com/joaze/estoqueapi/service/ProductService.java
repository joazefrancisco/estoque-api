package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductResponseDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productDto){
        Product product = new Product();

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setQuantity(0);
        product.setAverageCost(BigDecimal.ZERO);
        product.setTotalValue(BigDecimal.ZERO);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);

        return this.toResponseDto(product);
    }

    public Page<ProductResponseDto> findAll(Pageable pageable){
         return productRepository.findAll(pageable).map(this::toResponseDto);
    }

    public ProductDetailDto searchProduct(Long id){
        Product productData = this.findProductOrThrow(id);
        return new ProductDetailDto(
                productData.getId(),
                productData.getName(),
                productData.getDescription(),
                productData.getQuantity(),
                productData.getAverageCost(),
                productData.getTotalValue(),
                productData.getCreatedAt(),
                productData.getUpdatedAt()
        );
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productDto){
        Product productData = this.findProductOrThrow(id);

        productData.setName(productDto.name());
        productData.setDescription(productDto.description());
        productData.setUpdatedAt(LocalDateTime.now());

        return this.toResponseDto(productData);
    }

    public void deleteProduct(Long id){
        Product productData = this.findProductOrThrow(id);
        productRepository.delete(productData);
    }

    private Product findProductOrThrow(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private ProductResponseDto toResponseDto(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt()
        );
    }
}