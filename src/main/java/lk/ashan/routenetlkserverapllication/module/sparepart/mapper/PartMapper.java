package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Part;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        PartCategoryMapper.class, PartStatusMapper.class,UnitOfMeasureMapper.class, BranchMapper.class
})
public interface PartMapper {
    PartDetailResponseDto toDto(Part part);
    List<PartDetailResponseDto> toDtoList(List<Part> parts);
}
