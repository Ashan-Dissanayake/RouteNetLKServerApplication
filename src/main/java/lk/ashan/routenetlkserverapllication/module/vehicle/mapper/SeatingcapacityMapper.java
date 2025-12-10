package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.SeatingcapacityResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Seatingcapacity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        MakeMapper.class
})
public interface SeatingcapacityMapper {

    SeatingcapacityResponseDto toDto(Seatingcapacity seatingcapacity);
    List<SeatingcapacityResponseDto> toDtoList(List<Seatingcapacity> seatingcapacity);

}
