package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.DirectionDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Direction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DirectionMapper {
    Direction toEntity(DirectionDto directionDto);

    DirectionDto toDto(Direction direction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Direction partialUpdate(DirectionDto directionDto, @MappingTarget Direction direction);
}
