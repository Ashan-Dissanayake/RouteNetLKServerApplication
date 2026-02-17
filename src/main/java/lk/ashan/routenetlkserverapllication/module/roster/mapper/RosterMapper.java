package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        ShiftRosterAssignmentMapper.class,
        RosterStatusMapper.class
})
public interface RosterMapper {
    RosterDetailResponseDto toDto(Roster roster);
    List<RosterDetailResponseDto> toDtoList(List<Roster> rosters);
}
