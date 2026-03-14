package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnPartDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPart;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {PartMapper.class})
public interface GrnPartMapper {
    GrnPartDto toDto(GrnPart grnPart);
    List<GrnPartDto> toDtoList(List<GrnPart> grnParts);

    @Mapping(target = "grn", ignore = true)
    GrnPart toEntity(GrnPartDto dto);
}
