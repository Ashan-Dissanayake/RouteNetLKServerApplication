package lk.ashan.routenetlkserverapllication.module.roster.mapper;


import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftRosterAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        ShiftMapper.class,
        RoleMapper.class,
        EmployeeMapper.class,
        ShiftRosterAssignmentStatusMapper.class
})
public interface ShiftRosterAssignmentMapper {
    ShiftRosterAssignmentDto toDto(Shiftrosterassignment shiftRosterAssignment);
    List<ShiftRosterAssignmentDto> toDtoList(List<Shiftrosterassignment> shiftRosterAssignments);

    Shiftrosterassignment toEntity(ShiftRosterAssignmentDto shiftRosterAssignmentDto);

}
