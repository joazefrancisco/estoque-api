package com.joaze.estoqueapi.dto.service;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.exception.BusinessException;
import com.joaze.estoqueapi.exception.ResourceNotFoundException;
import com.joaze.estoqueapi.mapper.ProductMapper;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.model.ProductStatus;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Transactional
    public ProductSummaryDto createProduct(ProductRequestDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toSummaryDto(product);
    }

    public Page<ProductSummaryDto> findAll(Pageable pageable) {
        return productRepository
                .findByStatus(ProductStatus.ACTIVE, pageable)
                .map(productMapper::toSummaryDto);
    }

    public ProductDetailDto searchProduct(Long id) {
        Product productData = this.findProductOrThrow(id);
        return productMapper.toDetailDto(productData);
    }

    @Transactional
    public ProductSummaryDto updateProduct(Long id, ProductRequestDto productDto) {
        Product productData = this.findProductOrThrow(id);

        if (ProductStatus.INACTIVE.equals(productData.getStatus()))
            throw new BusinessException("Product inactive");

        productMapper.updateEntity(productData, productDto);
        return productMapper.toSummaryDto(productData);
    }

    @Transactional
    public void changeStatus(Long id, ProductStatus status) {
        Product productData = this.findProductOrThrow(id);

        if (productData.getStatus().equals(status))
            return;

        productData.setStatus(status);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id: " + id + " not found"));
    }
}