package lk.ashan.routenetlkserverapllication.module.partreqest.mapper;

import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequestitem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartRequestItemMapper {
    PartRequestItemDto toDto(Partrequestitem partRequestItem);
    List<PartRequestItemDto> toDtoList(List<Partrequestitem> partRequestItems);

    @Mapping(target = "partrequest", ignore = true)
    Partrequestitem toEntity(PartRequestItemDto dto);
}
