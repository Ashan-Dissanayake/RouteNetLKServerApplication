package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.DocumentstatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.DocumenttypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Documentstatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Documenttype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentstatusMapper {

    DocumentstatusDto toDto(Documentstatus documentstatus);
    List<DocumentstatusDto> toDtoList(List<Documentstatus> documentstatuses);

}
