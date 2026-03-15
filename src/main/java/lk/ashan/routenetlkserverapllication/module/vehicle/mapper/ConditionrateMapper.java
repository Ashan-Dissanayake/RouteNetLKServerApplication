package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ConditionrateDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.ConditionRate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConditionrateMapper {

    ConditionrateDto toDto(ConditionRate conditionrate);
    List<ConditionrateDto> toDtoList(List<ConditionRate> conditionRates);

}
