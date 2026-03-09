package lk.ashan.routenetlkserverapllication.module.partreqest.mapper;

import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestStatusDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        PartRequestStatusMapper.class, PartRequestItemMapper.class
})
public interface PartRequestMapper {
    PartRequestDetailResponseDto toDto(Partrequest partRequest);
    List<PartRequestDetailResponseDto> toDtoList(List<Partrequest> partRequests);

    Partrequest toEntity(PartRequestCreateRequestDto createRequestDto);
    Partrequest toEntity(PartRequestUpdateRequestDto updateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "partrequeststatus", ignore = true)
    @Mapping(target = "partrequestitems", ignore = true)
    void updateEntity(
            @MappingTarget Partrequest request,
            PartRequestUpdateRequestDto dto
    );
}
