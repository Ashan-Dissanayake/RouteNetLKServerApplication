package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.UnitOfMeasureDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Unitofmeasure;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitOfMeasureMapper {
    UnitOfMeasureDto toDto(Unitofmeasure unitOfMeasure);
    List<UnitOfMeasureDto> toDtoList(List<Unitofmeasure> unitOfMeasures);
}
