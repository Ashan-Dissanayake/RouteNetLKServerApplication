package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationContext;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitCreateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class PermitValidationContextBuilder {

    public PermitValidationContext buildForCreate(PermitCreateRequestDto requestDto) {
        return PermitValidationContext.builder()
                .permitNumber(requestDto.getNumber())
                .vehicleId(requestDto.getVehicle().getId())
                .routeId(requestDto.getRoute().getId())
                .vehicleId(requestDto.getVehicle().getId())
                .requestBranchId(requestDto.getBranch().getId())
                .serviceTypeId(requestDto.getServicetype().getId())
                .doissued(requestDto.getDoissued())
                .doexpired(requestDto.getDoexpired())
                .build();
    }

}
