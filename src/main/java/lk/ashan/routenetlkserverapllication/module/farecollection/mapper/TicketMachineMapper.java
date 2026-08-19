package lk.ashan.routenetlkserverapllication.module.farecollection.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.TicketMachineDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between TicketMachine entities and DTOs.
 * Utilizes MapStruct for automatic mapping and BranchMapper for nested mappings.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        BranchMapper.class
})
public interface TicketMachineMapper {

    /**
     * Converts a TicketMachine entity to a TicketMachineDto.
     *
     * @param ticketMachine the TicketMachine entity to be converted
     * @return the converted TicketMachineDto
     */
    TicketMachineDto toDto(TicketMachine ticketMachine);

    /**
     * Converts a list of TicketMachine entities to a list of TicketMachineDto objects.
     *
     * @param ticketMachines the list of TicketMachine entities to be converted
     * @return the list of converted TicketMachineDto objects
     */
    List<TicketMachineDto> toDtoList(List<TicketMachine> ticketMachines);
}
