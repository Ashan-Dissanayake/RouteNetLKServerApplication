package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftTypeDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shifttype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShiftTypeMapper {
  ShiftTypeDto toDto(Shifttype shiftType);
  List<ShiftTypeDto> toDetailList(List<Shifttype> shiftTypes);
}
