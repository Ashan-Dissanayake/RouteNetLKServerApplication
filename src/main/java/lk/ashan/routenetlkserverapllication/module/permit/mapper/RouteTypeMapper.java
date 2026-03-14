package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.RouteTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.RouteType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteTypeMapper {
    RouteTypeDto toDto(RouteType routeType);
    List<RouteTypeDto> toDtoList(List<RouteType> routeTypes);

}
