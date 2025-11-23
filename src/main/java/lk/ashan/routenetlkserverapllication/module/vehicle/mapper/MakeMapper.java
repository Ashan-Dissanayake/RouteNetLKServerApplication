package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.MakeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Make;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MakeMapper {

    MakeDto toDto(Make make);
    List<MakeDto> toDtoList(List<Make> makes);

}
