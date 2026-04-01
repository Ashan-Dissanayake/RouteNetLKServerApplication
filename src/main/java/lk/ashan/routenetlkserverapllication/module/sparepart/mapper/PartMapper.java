package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
       PartStatusMapper.class, BranchMapper.class,PartMasterMapper.class
})
public interface PartMapper {
    PartDetailResponseDto toDto(Part part);
    List<PartDetailResponseDto> toDtoList(List<Part> parts);

    Part toEntity(PartCreateRequestDto createRequestDto);

    @Mapping(target = "id", ignore = true) // updated in service via entity loaded from DB
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "partstatus", ignore = true)
    @Mapping(target = "partmaster", ignore = true)
    void updateFromDto(PartUpdateRequestDto dto, @MappingTarget Part entity);
}
