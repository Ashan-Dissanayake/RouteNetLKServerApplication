package lk.ashan.routenetlkserverapllication.module.roster.mapper;


import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftRosterAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftRosterAssignmentStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignmentstatus;
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
