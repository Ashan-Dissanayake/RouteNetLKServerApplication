package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.RoutefamiliaritylevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Routefamiliaritylevel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoutefamiliaritylevelMapper {
    RoutefamiliaritylevelDto toDto(Routefamiliaritylevel routefamiliaritylevel);
}
