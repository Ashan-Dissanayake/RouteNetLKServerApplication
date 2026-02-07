package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.Servicetype;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.BusTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Bustype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceTypeMapper {
    ServiceTypeDto toDto(Servicetype serviceType);
    List<ServiceTypeDto> toDtoList(List<Servicetype> serviceTypes);

}
