package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.RouteSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        RouteTypeMapper.class
})
public interface RouteMapper {

    @Mapping(target = "name", expression = "java(buildRouteName(route))")
    RouteSummaryResponseDto toDto(Route route);

    List<RouteSummaryResponseDto> toDtoList(List<Route> routes);

    default String buildRouteName(Route route) {
        return route.getNumber() + " " +
                route.getOrigin() + "-" +
                route.getDestination();
    }
}
