package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CrewStatusMapper {
    CrewStatusDto toDto(CrewStatus crewstatus);
    List<CrewStatusDto> toDtoList(List<CrewStatus> crewStatuses);
}
