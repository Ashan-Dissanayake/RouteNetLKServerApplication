package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.DesignationMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ShiftMapper.class, DesignationMapper.class
})
public interface RosterShiftMapper {
    RosterShiftSummaryDto toSummaryDto(RosterShift rosterShift);
    List<RosterShiftSummaryDto> toSummaryDtoList(List<RosterShift> rosterShifts);

    RosterShiftDetailResponseDto toDto(RosterShift rosterShift);
    List<RosterShiftDetailResponseDto> toDtoList(List<RosterShift> rosterShifts);
}
