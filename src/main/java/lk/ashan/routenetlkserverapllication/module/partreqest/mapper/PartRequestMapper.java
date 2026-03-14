package lk.ashan.routenetlkserverapllication.module.partreqest.mapper;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        PartRequestStatusMapper.class, PartRequestItemMapper.class
})
public interface PartRequestMapper {
    PartRequestDetailResponseDto toDto(PartRequest partRequest);
    List<PartRequestDetailResponseDto> toDtoList(List<PartRequest> partRequests);

    PartRequest toEntity(PartRequestCreateRequestDto createRequestDto);
    PartRequest toEntity(PartRequestUpdateRequestDto updateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "partrequeststatus", ignore = true)
    @Mapping(target = "partrequestitems", ignore = true)
    void updateEntity(
            @MappingTarget PartRequest request,
            PartRequestUpdateRequestDto dto
    );
}
