package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RosterStatusMapper {
  RosterStatusDto toDto(Rosterstatus rosterStatus);
  List<RosterStatusDto> toDtoList(List<Rosterstatus> rosterStatuses);
}
