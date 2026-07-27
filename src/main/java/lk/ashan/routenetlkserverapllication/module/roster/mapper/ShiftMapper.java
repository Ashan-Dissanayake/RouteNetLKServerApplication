package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShiftMapper {

    @Mapping(target = "shiftFullName", source = ".", qualifiedByName = "buildShiftFullName")
    ShiftSummaryDto toDto(Shift shift);

    List<ShiftSummaryDto> toDtoList(List<Shift> shifts);

    @Named("buildShiftFullName")
    default String buildShiftFullName(Shift shift) {
        return String.format(
                "%s (%s - %s)",
                shift.getName(),
                shift.getTostart(),
                shift.getToend()
        );
    }
}
