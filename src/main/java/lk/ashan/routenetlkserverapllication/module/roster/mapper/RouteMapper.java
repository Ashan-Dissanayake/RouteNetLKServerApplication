package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RouteDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteMapper {
  @Mapping(target = "name", expression = "java(route.getSource() + \"-\" + route.getDestination())")
  RouteDto toDto(Route route);
  List<RouteDto> toDtoList(List<Route> routes);
}
