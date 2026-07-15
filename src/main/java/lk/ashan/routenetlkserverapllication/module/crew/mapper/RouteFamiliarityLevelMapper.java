package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


/**
 * Mapper interface for converting between `RouteFamiliarityLevel` entities and DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteFamiliarityLevelMapper {

    /**
     * Converts a `RouteFamiliarityLevel` entity to a `RouteFamiliarityLevelDto`.
     *
     * @param routefamiliaritylevel the entity to be converted
     * @return the converted `RouteFamiliarityLevelDto`
     */
    RouteFamiliarityLevelDto toDto(RouteFamiliarityLevel routefamiliaritylevel);

    /**
     * Converts a list of `RouteFamiliarityLevel` entities to a list of `RouteFamiliarityLevelDto`s.
     *
     * @param routeFamiliarityLevels the list of entities to be converted
     * @return the list of converted `RouteFamiliarityLevelDto`s
     */
    List<RouteFamiliarityLevelDto> toDtoList(List<RouteFamiliarityLevel> routeFamiliarityLevels);

}
