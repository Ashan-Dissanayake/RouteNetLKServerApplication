package lk.ashan.routenetlkserverapllication.module.roster.mapper;


import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShiftMapper {

    @Mapping(target = "name",expression = "java(generateShiftName(shift))")
    ShiftSummaryResponseDto toDto(Shift shift);
    List<ShiftSummaryResponseDto> toDtoList(List<Shift> shifts);

    Shift toEntity(ShiftSummaryResponseDto shiftSummaryResponseDto);

    default String generateShiftName(Shift shift) {
        if (shift.getName() != null && shift.getTostart() != null && shift.getToend() != null) {
            return shift.getName() + " - " + shift.getTostart()+ " - " + shift.getToend();
        }
        return "Unnamed Shift";
    }

}
