package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between `Grn` entities and DTOs.
 * Utilizes MapStruct for object mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {GrnStatusMapper.class, GrnPartRequestItemMapper.class})
public interface GrnMapper {

    /**
     * Converts a `Grn` entity to a `GrnDetailResponseDto`.
     *
     * @param grn the `Grn` entity to convert
     * @return the converted `GrnDetailResponseDto`
     */
    GrnDetailResponseDto toDto(Grn grn);

    /**
     * Converts a list of `Grn` entities to a list of `GrnDetailResponseDto`.
     *
     * @param grns the list of `Grn` entities to convert
     * @return the list of converted `GrnDetailResponseDto`
     */
    List<GrnDetailResponseDto> toDtoList(List<Grn> grns);

    /**
     * Updates an existing `Grn` entity with data from a `GrnUpdateRequestDto`.
     * Fields specified with `@Mapping` are ignored during the update.
     *
     * @param grn the target `Grn` entity to update
     * @param dto the `GrnUpdateRequestDto` containing the update data
     * @return the updated `Grn` entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "partrequest", ignore = true)
    @Mapping(target = "grnstatus", ignore = true)
    @Mapping(target = "grnpartrequestitems", ignore = true)
    Grn updateEntityFromDto(@MappingTarget Grn grn, GrnUpdateRequestDto dto);

}
