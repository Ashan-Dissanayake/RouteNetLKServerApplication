package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripCrewAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        TripMapper.class,
        EmployeeMapper.class,
        TripAllocationStatusMapper.class
})
public interface TripCrewAllocationMapper {
    List<TripCrewAllocationDetailResponseDto> toDtoList(List<TripCrewAllocation> tripCrewAllocations);
    TripCrewAllocationDetailResponseDto toDto(TripCrewAllocation tripcrewallocation);
}
