package lk.ashan.routenetlkserverapllication.module.partreqest.mapper;

import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestStatusDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        PartRequestStatusMapper.class, PartRequestItemMapper.class
})
public interface PartRequestMapper {
    PartRequestDetailResponseDto toDto(Partrequest partRequest);
    List<PartRequestDetailResponseDto> toDtoList(List<Partrequest> partRequests);

    Partrequest toEntity(PartRequestCreateRequestDto createRequestDto);
}
