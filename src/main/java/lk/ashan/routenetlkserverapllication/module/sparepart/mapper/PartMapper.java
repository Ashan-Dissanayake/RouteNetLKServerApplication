package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Part;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        PartCategoryMapper.class, PartStatusMapper.class,UnitOfMeasureMapper.class, BranchMapper.class
})
public interface PartMapper {
    PartDetailResponseDto toDto(Part part);
    List<PartDetailResponseDto> toDtoList(List<Part> parts);

    Part toEntity(PartCreateRequestDto createRequestDto);

    @Mapping(target = "id", ignore = true) // updated in service via entity loaded from DB
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "partcategory", ignore = true)
    @Mapping(target = "qoh", ignore = true)
    @Mapping(target = "dolastordered", ignore = true)
    void updateFromDto(PartUpdateRequestDto dto, @MappingTarget Part entity);
}
