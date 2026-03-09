package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grnstatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrnStatusMapper {
    GrnStatusDto toDto(Grnstatus grnStatus);
    List<GrnStatusDto> toDtoList(List<Grnstatus> grnStatuses);
}
