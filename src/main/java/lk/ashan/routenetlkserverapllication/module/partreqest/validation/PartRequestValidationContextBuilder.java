package lk.ashan.routenetlkserverapllication.module.partreqest.validation;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class PartRequestValidationContextBuilder {

    public PartRequestValidationContext buildForCreate(PartRequestCreateRequestDto dto) {
        return PartRequestValidationContext.builder()
                .branchId(dto.getBranch().getId())
                .requestedate(dto.getDorequested())
                .items(dto.getPartrequestitems())
                .build();
    }
}
