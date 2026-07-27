package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OriginTerminalDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between `Originterminal` entities and `OriginTerminalDto` DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OriginTerminalMapper {

    /**
     * Converts an `OriginTerminalDto` to an `Originterminal` entity.
     *
     * @param originTerminalDto the DTO to be converted
     * @return the corresponding `Originterminal` entity
     */
    Originterminal toEntity(OriginTerminalDto originTerminalDto);

    /**
     * Converts an `Originterminal` entity to an `OriginTerminalDto`.
     *
     * @param originTerminal the entity to be converted
     * @return the corresponding `OriginTerminalDto`
     */
    OriginTerminalDto toDto(Originterminal originTerminal);

    /**
     * Converts a list of `Originterminal` entities to a list of `OriginTerminalDto` DTOs.
     *
     * @param originTerminals the list of entities to be converted
     * @return the corresponding list of `OriginTerminalDto` DTOs
     */
    List<OriginTerminalDto> toDtoList(List<Originterminal> originTerminals);
}
