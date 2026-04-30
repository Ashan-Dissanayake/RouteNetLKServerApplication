package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RosterShiftAssignmentResponseDto {
    private Integer id;
    private String rosterName;
    private String shiftName;
    private String employeeName;
    private String employeeNumber;
    private String designation;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
