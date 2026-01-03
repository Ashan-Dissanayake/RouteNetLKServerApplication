package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ShiftTypeMapper.class, BranchMapper.class
})
public interface RosterMapper {
  RosterDetailResponseDto toDto(Roster roster);
  Roster toEntity(RosterDetailResponseDto rosterDetailResponseDto);
  List<RosterDetailResponseDto> toDetailList(List<Roster> rosters);
}
