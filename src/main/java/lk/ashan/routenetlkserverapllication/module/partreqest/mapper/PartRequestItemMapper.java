package lk.ashan.routenetlkserverapllication.module.partreqest.mapper;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {PartMapper.class})
public interface PartRequestItemMapper {
    PartRequestItemDto toDto(PartRequestItem partRequestItem);
    List<PartRequestItemDto> toDtoList(List<PartRequestItem> partRequestItems);

    @Mapping(target = "partrequest", ignore = true)
    @Mapping(target = "id", ignore = true)
    PartRequestItem toEntity(PartRequestItemDto dto);
}
