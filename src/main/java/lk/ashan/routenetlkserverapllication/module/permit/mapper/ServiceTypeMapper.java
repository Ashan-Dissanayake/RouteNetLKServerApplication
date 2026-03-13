package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Servicetype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceTypeMapper {
    ServiceTypeDto toDto(Servicetype serviceType);
    List<ServiceTypeDto> toDtoList(List<Servicetype> serviceTypes);

}
