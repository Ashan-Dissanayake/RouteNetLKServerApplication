package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EligibleCrewDto {
    private Integer id;
    private String employeeName;
    private String employeeNumber;
    private RouteFamiliarityLevelDto routeFamiliarityLevel;
    private String shiftName;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
}
