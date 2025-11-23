package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.ServicetypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Servicetype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServicetypeMapper {

    ServicetypeDto toDto(Servicetype servicetype);
    List<ServicetypeDto> toDtoList(List<Servicetype> servicetypes);

}
