package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RosterShiftSummaryDto {
    private Integer id;
    private Shift shift;
    private LocalDate doshift;
    private DesignationDto designation;
}
