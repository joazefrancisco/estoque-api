package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.model.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface MovementMapper {

    @Mapping(target = "movementId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "remainingQuantity", source = "product.quantity")
    MovementResponseDto toResponseDto(Movement movement);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "date", source = "createdAt")
    MovementSummaryDto toSummaryDto(Movement movement);

    MovementDetailDto toDetailDto(Movement movement);
}
