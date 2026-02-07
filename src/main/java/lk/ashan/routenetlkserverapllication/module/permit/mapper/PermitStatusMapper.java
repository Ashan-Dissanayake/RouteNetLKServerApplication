package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitStatusDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermitStatusMapper {
    PermitStatusDto toDto(Permitestatus permiteStatus);
    List<PermitStatusDto> toDtoList(List<Permitestatus> permitStatuses);

}
