package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class ConductorValidationContext {
    private Integer id;
    private Integer employeeId;
    private LocalDate medicalIssued;
    private LocalDate medicalExpired;
}


