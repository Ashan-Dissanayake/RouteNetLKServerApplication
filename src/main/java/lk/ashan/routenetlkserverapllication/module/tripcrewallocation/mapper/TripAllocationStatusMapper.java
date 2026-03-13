package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripallocationstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripAllocationStatusMapper {
    Tripallocationstatus toEntity(TripAllocationStatusDto tripAllocationStatusDto);

    TripAllocationStatusDto toDto(Tripallocationstatus tripAllocationStatus);
    List<TripAllocationStatusDto> toDtoList(List<Tripallocationstatus> tripAllocationStatuses);
}
