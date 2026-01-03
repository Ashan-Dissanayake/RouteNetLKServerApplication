package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ShiftTypeMapper.class, ShiftStatusMapper.class
})
public interface ShiftMapper {
  ShiftDto toDto(Shift shift);
  List<ShiftDto> toDetailList(List<Shift> Shifts);
}
