package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.DocumenttypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Documenttype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumenttypeMapper {

    DocumenttypeDto toDto(Documenttype documenttype);
    List<DocumenttypeDto> toDtoList(List<Documenttype> documenttypes);

}
