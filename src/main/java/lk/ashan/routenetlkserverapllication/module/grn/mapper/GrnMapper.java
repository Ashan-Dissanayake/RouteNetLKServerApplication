package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grn;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {GrnStatusMapper.class, GrnPartMapper.class})
public interface GrnMapper {
    GrnDetailResponseDto toDto(Grn grn);
    List<GrnDetailResponseDto> toDtoList(List<Grn> grns);

    Grn toEntity(GrnCreateRequestDto createRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "grnstatus", ignore = true)
    @Mapping(target = "grnparts", ignore = true)
    void updateEntity(@MappingTarget Grn grn, GrnUpdateRequestDto dto);

}
