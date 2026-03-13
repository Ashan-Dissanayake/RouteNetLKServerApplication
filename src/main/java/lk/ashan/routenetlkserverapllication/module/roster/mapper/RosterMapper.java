package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
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

    Roster toEntity(RosterCreateRequestDto rosterCreateRequestDto);
    Roster toEntity(RosterUpdateRequestDto rosterUpdateRequestDto);
}
