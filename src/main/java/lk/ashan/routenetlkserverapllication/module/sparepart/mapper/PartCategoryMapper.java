package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartCategoryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Partcategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartCategoryMapper {
    PartCategoryDto toDto(Partcategory parCategory);
    List<PartCategoryDto> toDtoList(List<Partcategory> parCategories);
}
