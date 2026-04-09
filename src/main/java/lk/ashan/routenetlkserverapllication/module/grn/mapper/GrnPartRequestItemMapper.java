package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnPartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPartRequestItem;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {PartMapper.class, PartRequestMapper.class})
public interface GrnPartRequestItemMapper {
    GrnPartRequestItemDto toDto(GrnPartRequestItem grnPart);
    List<GrnPartRequestItemDto> toDtoList(List<GrnPartRequestItem> grnParts);

//    @Mapping(target = "grn", ignore = true)
//    GrnPartRequestItem toEntity(GrnPartRequestItemDto dto);
}
