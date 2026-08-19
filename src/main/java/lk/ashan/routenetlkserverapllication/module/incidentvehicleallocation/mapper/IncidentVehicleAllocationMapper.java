package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between IncidentVehicleAllocation entities and DTOs.
 * Utilizes other mappers for related entities.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        IncidentVehicleAllocationStatusMapper.class, VehicleMapper.class, BranchMapper.class, IncidentMapper.class
})
public interface IncidentVehicleAllocationMapper {

    /**
     * Converts an IncidentVehicleAllocation entity to a DTO.
     *
     * @param incidentVehicleAllocation the IncidentVehicleAllocation entity to convert
     * @return the converted IncidentVehicleAllocationDetailsResponseDto
     */
    IncidentVehicleAllocationDetailsResponseDto toDto(IncidentVehicleAllocation incidentVehicleAllocation);

    /**
     * Converts a list of IncidentVehicleAllocation entities to a list of DTOs.
     *
     * @param incidentVehicleAllocations the list of IncidentVehicleAllocation entities to convert
     * @return the list of converted IncidentVehicleAllocationDetailsResponseDto
     */
    List<IncidentVehicleAllocationDetailsResponseDto> toDtoList(List<IncidentVehicleAllocation> incidentVehicleAllocations);

    /**
     * Converts an IncidentVehicleAllocationCreateRequestDto to an IncidentVehicleAllocation entity.
     *
     * @param incidentVehicleAllocationCreateRequestDto the DTO to convert
     * @return the converted IncidentVehicleAllocation entity
     */
    IncidentVehicleAllocation toEntity(IncidentVehicleAllocationCreateRequestDto incidentVehicleAllocationCreateRequestDto);
}
