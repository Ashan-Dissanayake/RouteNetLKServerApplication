package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ConditionrateDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Conditionrate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConditionrateMapper {

    ConditionrateDto toDto(Conditionrate conditionrate);
    List<ConditionrateDto> toDtoList(List<Conditionrate> conditionrates);

}
