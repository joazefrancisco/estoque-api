package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto productRequestDto){
        Product product = new Product();
        product.setName(productRequestDto.name());
        product.setDescription(productRequestDto.description());
        return product;
    }

    public ProductSummaryDto toProductSummaryDto(Product product){
        return new ProductSummaryDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt()
        );
    }

    public ProductDetailDto toProductDetailDto(Product product){
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