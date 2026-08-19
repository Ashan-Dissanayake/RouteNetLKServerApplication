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

/**
 * Mapper interface for converting between `GrnPartRequestItem` entities and `GrnPartRequestItemDto` DTOs.
 * Utilizes `PartMapper` and `PartRequestMapper` for nested mappings.
 * Unmapped target properties are ignored.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PartMapper.class, PartRequestMapper.class})
public interface GrnPartRequestItemMapper {

    /**
     * Converts a `GrnPartRequestItem` entity to a `GrnPartRequestItemDto`.
     *
     * @param grnPart the `GrnPartRequestItem` entity to convert
     * @return the converted `GrnPartRequestItemDto`
     */
    GrnPartRequestItemDto toDto(GrnPartRequestItem grnPart);

    /**
     * Converts a list of `GrnPartRequestItem` entities to a list of `GrnPartRequestItemDto` DTOs.
     *
     * @param grnParts the list of `GrnPartRequestItem` entities to convert
     * @return the list of converted `GrnPartRequestItemDto` DTOs
     */
    List<GrnPartRequestItemDto> toDtoList(List<GrnPartRequestItem> grnParts);

}
