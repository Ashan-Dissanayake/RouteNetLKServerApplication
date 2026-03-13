package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ScheduleTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Scheduletype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleTypeMapper {
    ScheduleTypeDto toDto(Scheduletype scheduletype);
    List<ScheduleTypeDto> toDtoList(List<Scheduletype> scheduletypes);

}
