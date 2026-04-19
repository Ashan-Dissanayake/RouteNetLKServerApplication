package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripCrewAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripAllocationStatusMapper {
    TripCrewAllocationStatus toEntity(TripCrewAllocationStatusDto tripCrewAllocationStatusDto);

    TripCrewAllocationStatusDto toDto(TripCrewAllocationStatus tripAllocationStatus);
    List<TripCrewAllocationStatusDto> toDtoList(List<TripCrewAllocationStatus> tripAllocationStatuses);
}
