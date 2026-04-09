package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.mapper.ProductMapper;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    @Transactional
    public ProductSummaryDto createProduct(ProductRequestDto productDto){
        Product product = ProductMapper.toEntity(productDto);
        productRepository.save(product);
        return ProductMapper.toProductSummaryDto(product);
    }

    public Page<ProductSummaryDto> findAll(Pageable pageable){
         return productRepository.findAll(pageable).map(ProductMapper::toProductSummaryDto);
    }

    public ProductDetailDto searchProduct(Long id){
        Product productData = this.findProductOrThrow(id);
        return ProductMapper.toProductDetailDto(productData);
    }

    @Transactional
    public ProductSummaryDto updateProduct(Long id, ProductRequestDto productDto){
        Product productData = this.findProductOrThrow(id);

        productData.setName(productDto.name());
        productData.setDescription(productDto.description());
        productData.setUpdatedAt(LocalDateTime.now());

        return ProductMapper.toProductSummaryDto(productData);
    }

    public void deleteProduct(Long id){
        Product productData = this.findProductOrThrow(id);
        productRepository.delete(productData);
    }

    private Product findProductOrThrow(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}