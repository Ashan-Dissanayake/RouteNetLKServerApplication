package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class AllocationContextBuilder {

    public AllocationContext buildForCreate(IncidentVehicleAllocationCreateRequestDto dto) {
        return AllocationContext.builder()
                .incidentId(dto.getIncident().getId())
                .vehicleId(dto.getVehicle().getId())
                .branchId(dto.getProvidebranch().getId())
                .build();
    }
}
