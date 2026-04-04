package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.ProductResponseDto;
import com.joaze.estoqueapi.dto.ProductRequestDto;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productDto){
        Product product = new Product();

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setQuantity(productDto.quantity());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);

        return this.toResponseDto(product);
    }

    public List<ProductResponseDto> findAll(){
         return productRepository.findAll().stream()
                 .map(this::toResponseDto)
                 .toList();
    }

    public ProductResponseDto searchProduct(Long id){
        Product productData = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return this.toResponseDto(productData);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productDto){
        Product productData = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productData.setName(productDto.name());
        productData.setDescription(productDto.description());
        productData.setPrice(productDto.price());
        productData.setQuantity(productDto.quantity());
        productData.setUpdatedAt(LocalDateTime.now());

        return this.toResponseDto(productData);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        productRepository.delete(product);
    }

    private ProductResponseDto toResponseDto(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}