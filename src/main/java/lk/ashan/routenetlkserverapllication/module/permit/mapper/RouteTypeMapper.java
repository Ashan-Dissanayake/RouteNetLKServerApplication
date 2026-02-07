package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.dto.RouteTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.Routetype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteTypeMapper {
    RouteTypeDto toDto(Routetype routeType);
    List<RouteTypeDto> toDtoList(List<Routetype> routeTypes);

}
