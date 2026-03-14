package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ScheduleTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.ScheduleType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleTypeMapper {
    ScheduleTypeDto toDto(ScheduleType scheduletype);
    List<ScheduleTypeDto> toDtoList(List<ScheduleType> scheduleTypes);

}
