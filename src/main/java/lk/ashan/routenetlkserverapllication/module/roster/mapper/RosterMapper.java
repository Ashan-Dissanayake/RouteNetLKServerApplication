package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        RosterShiftMapper.class
})
public interface RosterMapper {

    Roster toEntity(RosterRequestDto requestDto);

    @Mapping(target = "name", expression = "java(roster.getDostartofweek() + \" - \" + roster.getDoendofweek())")
    RosterSummaryDto toSummaryDto(Roster roster);
    List<RosterSummaryDto> toSummaryDto(List<Roster> rosters);
}
