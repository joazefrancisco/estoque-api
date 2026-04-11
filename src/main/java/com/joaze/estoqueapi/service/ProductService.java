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

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    private ProductMapper productMapper;

    @Transactional
    public ProductSummaryDto createProduct(ProductRequestDto productDto){
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toSummaryDto(product);
    }

    public Page<ProductSummaryDto> findAll(Pageable pageable){
         return productRepository.findAll(pageable).map(productMapper::toSummaryDto);
    }

    public ProductDetailDto searchProduct(Long id){
        Product productData = this.findProductOrThrow(id);
        return productMapper.toDetailDto(productData);
    }

    @Transactional
    public ProductSummaryDto updateProduct(Long id, ProductRequestDto productDto){
        Product productData = this.findProductOrThrow(id);
        productMapper.updateEntity(productData, productDto);
        return productMapper.toSummaryDto(productData);
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