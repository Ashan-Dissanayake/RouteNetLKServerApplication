package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Crewstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CrewStatusMapper {
    CrewStatusDto toDto(Crewstatus crewstatus);
    List<CrewStatusDto> toDtoList(List<Crewstatus> crewstatuses);
}
