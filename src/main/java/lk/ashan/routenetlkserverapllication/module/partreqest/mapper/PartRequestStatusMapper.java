package lk.ashan.routenetlkserverapllication.module.partreqest.mapper;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestStatusDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface  PartRequestStatusMapper {
    PartRequestStatusDto toDto(PartRequestStatus partRequestStatus);
    List<PartRequestStatusDto> toDtoList(List<PartRequestStatus> partRequestStatuses);
}
