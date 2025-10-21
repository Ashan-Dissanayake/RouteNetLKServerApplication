package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenderMapper {

    GenderDto toDto(Gender gender);
    List<GenderDto> toDtoList(List<Gender> genders);


}
