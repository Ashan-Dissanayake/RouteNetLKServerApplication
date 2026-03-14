package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitStatusDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermitStatusMapper {
    PermitStatusDto toDto(PermiteStatus permiteStatus);
    List<PermitStatusDto> toDtoList(List<PermiteStatus> permitStatuses);

}
