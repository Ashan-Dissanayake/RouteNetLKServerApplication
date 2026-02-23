package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RoleMapper;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftMapper;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.dto.TripCrewAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripcrewallocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        TripMapper.class,
        EmployeeMapper.class,
        RoleMapper.class,
        ShiftMapper.class,
        TripAllocationStatusMapper.class
})
public interface TripCrewAllocationMapper {
    TripCrewAllocationDetailResponseDto toDto(Tripcrewallocation tripCrewAllocation);
    List<TripCrewAllocationDetailResponseDto> toDtoList(List<Tripcrewallocation> tripCrewAllocations);
}
