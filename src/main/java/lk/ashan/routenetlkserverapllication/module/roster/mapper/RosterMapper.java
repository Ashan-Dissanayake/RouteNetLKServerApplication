package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ShiftTypeMapper.class, BranchMapper.class
})
public interface RosterMapper {
  RosterDto toDto(Roster roster);
  Roster toEntity(RosterDto rosterDto);
  List<RosterDto> toDetailList(List<Roster> rosters);
}
