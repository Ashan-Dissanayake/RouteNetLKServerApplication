package lk.ashan.routenetlkserverapllication.module.driver.mapper;

import lk.ashan.routenetlkserverapllication.module.driver.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.driver.model.Routefamiliaritylevel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteFamiliarityLevelMapper {
    RouteFamiliarityLevelDto toDto(Routefamiliaritylevel routefamiliaritylevel);
    List<RouteFamiliarityLevelDto> toDtoList(List<Routefamiliaritylevel> routeFamiliarityLevels);

}
