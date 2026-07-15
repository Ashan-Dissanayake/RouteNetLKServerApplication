package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


/**
 * Mapper interface for converting CrewStatus entities to CrewStatusDto objects.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CrewStatusMapper {

    /**
     * Converts a CrewStatus entity to a CrewStatusDto.
     *
     * @param crewstatus the CrewStatus entity to be converted
     * @return the converted CrewStatusDto
     */
    CrewStatusDto toDto(CrewStatus crewstatus);

    /**
     * Converts a list of CrewStatus entities to a list of CrewStatusDto objects.
     *
     * @param crewStatuses the list of CrewStatus entities to be converted
     * @return the list of converted CrewStatusDto objects
     */
    List<CrewStatusDto> toDtoList(List<CrewStatus> crewStatuses);
}
