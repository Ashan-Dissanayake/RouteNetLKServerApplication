package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.dto.ScheduleTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.Scheduletype;
import lk.ashan.routenetlkserverapllication.module.permit.model.Servicetype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleTypeMapper {
    ScheduleTypeDto toDto(Scheduletype scheduletype);
    List<ScheduleTypeDto> toDtoList(List<Scheduletype> scheduletypes);

}
