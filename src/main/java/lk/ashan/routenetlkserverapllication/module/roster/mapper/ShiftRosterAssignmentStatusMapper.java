package lk.ashan.routenetlkserverapllication.module.roster.mapper;


import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftRosterAssignmentStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignmentstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShiftRosterAssignmentStatusMapper {
    ShiftRosterAssignmentStatusDto toDto(Shiftrosterassignmentstatus shiftRosterAssignmentStatus);
    List<ShiftRosterAssignmentStatusDto> toDtoList(List<Shiftrosterassignmentstatus> shiftRosterAssignmentStatuses);

    Shiftrosterassignmentstatus toEntity(ShiftRosterAssignmentStatusDto shiftRosterAssignmentStatusDto);

}
