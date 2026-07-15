package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting between Conductor entities and DTOs.
 * Utilizes other mappers such as EmployeeMapper, RouteFamiliarityLevelMapper, and CrewStatusMapper.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        EmployeeMapper.class, RouteFamiliarityLevelMapper.class, CrewStatusMapper.class
})
public interface ConductorMapper {

    /**
     * Converts a list of Conductor entities to a list of ConductorDetailResponseDto.
     *
     * @param conductors the list of Conductor entities to be converted
     * @return a list of ConductorDetailResponseDto
     */
    List<ConductorDetailResponseDto> toDtoList(List<Conductor> conductors);

    /**
     * Converts a single Conductor entity to a ConductorDetailResponseDto.
     *
     * @param conductor the Conductor entity to be converted
     * @return the corresponding ConductorDetailResponseDto
     */
    ConductorDetailResponseDto toDto(Conductor conductor);

    /**
     * Converts a ConductorCreateRequestDto to a Conductor entity.
     *
     * @param dto the ConductorCreateRequestDto containing the data
     * @return the corresponding Conductor entity
     */
    Conductor toEntity(ConductorCreateRequestDto dto);

    /**
     * Converts a ConductorUpdateRequestDto to a Conductor entity.
     *
     * @param dto the ConductorUpdateRequestDto containing the data
     * @return the corresponding Conductor entity
     */
    Conductor toEntity(ConductorUpdateRequestDto dto);

    /**
     * Updates an existing Conductor entity with data from a ConductorUpdateRequestDto.
     * Fields such as id, routefamiliaritylevel, and crewstatus are ignored during the update.
     *
     * @param dto    the ConductorUpdateRequestDto containing the updated data
     * @param entity the existing Conductor entity to be updated
     * @return the updated Conductor entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "routefamiliaritylevel", ignore = true)
    @Mapping(target = "crewstatus", ignore = true)
    Conductor updateEntityFromDto(ConductorUpdateRequestDto dto, @MappingTarget Conductor entity);
}
