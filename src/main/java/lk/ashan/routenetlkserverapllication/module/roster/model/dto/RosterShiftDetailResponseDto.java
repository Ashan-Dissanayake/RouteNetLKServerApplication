package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RosterShiftDetailResponseDto {
    private Integer id;
    private ShiftSummaryDto shift;
    private LocalDate doshift;
    private DesignationDto designation;
    private Integer requiredemployeecount;
}
