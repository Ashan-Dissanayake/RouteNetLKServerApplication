package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between GrnStatus entity and GrnStatusDto.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrnStatusMapper {

    /**
     * Converts a GrnStatus entity to a GrnStatusDto.
     *
     * @param grnStatus the GrnStatus entity to be converted
     * @return the converted GrnStatusDto
     */
    GrnStatusDto toDto(GrnStatus grnStatus);

    /**
     * Converts a list of GrnStatus entities to a list of GrnStatusDto objects.
     *
     * @param grnStatuses the list of GrnStatus entities to be converted
     * @return the list of converted GrnStatusDto objects
     */
    List<GrnStatusDto> toDtoList(List<GrnStatus> grnStatuses);
}
