package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "averageCost", ignore = true)
    @Mapping(target = "totalValue", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDto productRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Product product, ProductRequestDto productRequestDto);

    ProductSummaryDto toSummaryDto(Product product);

    ProductDetailDto toDetailDto(Product product);
}