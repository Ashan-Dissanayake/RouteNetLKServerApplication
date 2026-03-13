package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ConductorContextBuilder {

    public ConductorValidationContext buildForCreate(ConductorCreateRequestDto dto) {
        return ConductorValidationContext.builder()
                .number(dto.getNumber())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }

    public ConductorValidationContext buildForUpdate(ConductorUpdateRequestDto dto) {
        return ConductorValidationContext.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }
}
