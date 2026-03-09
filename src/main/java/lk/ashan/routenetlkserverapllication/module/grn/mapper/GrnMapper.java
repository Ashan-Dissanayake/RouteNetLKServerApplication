package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grn;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {GrnStatusMapper.class, GrnPartMapper.class})
public interface GrnMapper {
    GrnDetailResponseDto toDto(Grn grn);
    List<GrnDetailResponseDto> toDtoList(List<Grn> grns);

    Grn toEntity(GrnCreateRequestDto createRequestDto);

}
