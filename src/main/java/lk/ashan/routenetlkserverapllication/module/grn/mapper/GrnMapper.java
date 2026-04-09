package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {GrnStatusMapper.class, GrnPartRequestItemMapper.class})
public interface GrnMapper {
    GrnDetailResponseDto toDto(Grn grn);
    List<GrnDetailResponseDto> toDtoList(List<Grn> grns);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "partrequest", ignore = true)
    @Mapping(target = "grnstatus", ignore = true)
    @Mapping(target = "grnpartrequestitems", ignore = true)
    Grn updateEntityFromDto(@MappingTarget Grn grn, GrnUpdateRequestDto dto);

}
