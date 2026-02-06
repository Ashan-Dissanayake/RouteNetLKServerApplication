package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.MakeRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.ModelDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Make;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Model;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ModelMapper {

    ModelDto toDto(Model model);
    List<ModelDto> toDtoList(List<Model> models);

}
