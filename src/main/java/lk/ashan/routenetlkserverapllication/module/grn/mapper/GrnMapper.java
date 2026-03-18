package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {GrnStatusMapper.class, GrnPartMapper.class})
public interface GrnMapper {
    GrnDetailResponseDto toDto(Grn grn);
    List<GrnDetailResponseDto> toDtoList(List<Grn> grns);

    Grn toEntity(GrnCreateRequestDto createRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "grnparts", ignore = true)
    Grn updateEntityFromDto(@MappingTarget Grn grn, GrnUpdateRequestDto dto);

}
