package com.joaze.estoqueapi.mapper;

import com.joaze.estoqueapi.dto.movement.*;
import com.joaze.estoqueapi.model.Movement;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementResponseDto toResponseDto(Movement movement);

    MovementSummaryDto toSummaryDto(Movement movement);

    MovementDetailDto toDetailDto(Movement movement);
}
