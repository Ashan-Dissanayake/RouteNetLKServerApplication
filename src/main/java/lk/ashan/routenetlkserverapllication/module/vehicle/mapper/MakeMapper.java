package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.MakeRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Make;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MakeMapper {

    MakeRequestDto toDto(Make make);
    Make toEntity(MakeRequestDto makeRequestDto);
    List<MakeRequestDto> toDtoList(List<Make> makes);

}
