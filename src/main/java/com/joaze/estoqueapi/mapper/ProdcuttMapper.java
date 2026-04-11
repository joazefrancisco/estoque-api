package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.product.ProductDetailDto;
import com.joaze.estoqueapi.dto.product.ProductRequestDto;
import com.joaze.estoqueapi.dto.product.ProductSummaryDto;
import com.joaze.estoqueapi.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdcuttMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", expression = "java(java.time.LocalDateTime.now())")
    Product toEntity(ProductRequestDto productRequestDto);

    void requestDtoUpdateEntity(Product product, ProductRequestDto productRequestDto);

    ProductSummaryDto toSummaryDto(Product product);

    ProductDetailDto toDetailDto(Product product);
}
