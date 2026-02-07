package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.dto.RouteDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ScheduleTypeMapper.class,RouteTypeMapper.class
})
public interface RouteMapper {
    RouteDto toDto(Route route);
    List<RouteDto> toDtoList(List<Route> routes);

}
