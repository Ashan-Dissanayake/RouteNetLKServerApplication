package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.CrewstatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Crewstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CrewstatusMapper {
    CrewstatusDto toDto(Crewstatus crewstatus);
}
