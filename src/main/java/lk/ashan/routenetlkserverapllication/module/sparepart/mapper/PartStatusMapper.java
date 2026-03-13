package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartStatusMapper {
    PartStatusDto toDto(Partstatus partStatus);
    List<PartStatusDto> toDtoList(List<Partstatus> partStatuses);
}
