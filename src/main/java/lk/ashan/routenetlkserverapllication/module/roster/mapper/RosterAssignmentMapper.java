package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        RosterMapper.class, EmployeeMapper.class,RosterAssignmentStatusMapper.class
}
)
public interface RosterAssignmentMapper {
  RosterAssignmentDto toDto(Rosterassignement rosterAssignment);
  List<RosterAssignmentDto> toDetailList(List<Rosterassignement> rosterAssignments);
}
