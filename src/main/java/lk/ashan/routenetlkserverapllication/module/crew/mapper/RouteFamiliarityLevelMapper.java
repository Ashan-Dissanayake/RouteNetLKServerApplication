package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Routefamiliaritylevel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteFamiliarityLevelMapper {
    RouteFamiliarityLevelDto toDto(Routefamiliaritylevel routefamiliaritylevel);
    List<RouteFamiliarityLevelDto> toDtoList(List<Routefamiliaritylevel> routeFamiliarityLevels);

}
