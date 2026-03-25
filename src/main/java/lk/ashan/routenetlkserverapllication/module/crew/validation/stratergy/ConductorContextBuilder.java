package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ConductorContextBuilder {

    public ConductorValidationContext buildForCreate(ConductorCreateRequestDto dto) {
        return ConductorValidationContext.builder()
                .employeeId(dto.getEmployee().getId())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }

    public ConductorValidationContext buildForUpdate(ConductorUpdateRequestDto dto) {
        return ConductorValidationContext.builder()
                .id(dto.getId())
                .employeeId(dto.getEmployee().getId())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }

}
