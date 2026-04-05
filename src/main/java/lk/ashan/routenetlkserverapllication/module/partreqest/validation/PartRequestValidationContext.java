package lk.ashan.routenetlkserverapllication.module.partreqest.validation;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class PartRequestValidationContext {
    private final Integer partRequestId;
    private final Integer branchId;
    private final LocalDate requestedate;
    private final List<PartRequestItemDto> items;
}
