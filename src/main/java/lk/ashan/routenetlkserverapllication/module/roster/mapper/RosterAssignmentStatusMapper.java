package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterAssignmentStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassigementstatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RosterAssignmentStatusMapper {
  RosterAssignmentStatusDto toDto(Rosterassigementstatus rosterAssigementStatus);
  List<RosterAssignmentStatusDto> toDetailList(List<Rosterassigementstatus> rosterAssignmentStatuses);
}
