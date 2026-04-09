package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto productRequestDto){
        Product product = new Product();
        product.setName(productRequestDto.name());
        product.setDescription(productRequestDto.description());
        product.setQuantity(0);
        product.setAverageCost(BigDecimal.ZERO);
        product.setTotalValue(BigDecimal.ZERO);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    public static ProductSummaryDto toProductSummaryDto(Product product){
        return new ProductSummaryDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt()
        );
    }

    public static ProductDetailDto toProductDetailDto(Product product){
        return new ProductDetailDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getAverageCost(),
                product.getTotalValue(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}