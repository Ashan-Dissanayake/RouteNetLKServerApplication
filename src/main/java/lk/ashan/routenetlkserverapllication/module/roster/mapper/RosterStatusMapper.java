package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RosterStatusMapper {
    Rosterstatus toEntity(RosterStatusDto rosterStatusDto);

    RosterStatusDto toDto(Rosterstatus rosterStatus);
    List<RosterStatusDto> toDtoList(List<Rosterstatus> rosterStatuses);
}
