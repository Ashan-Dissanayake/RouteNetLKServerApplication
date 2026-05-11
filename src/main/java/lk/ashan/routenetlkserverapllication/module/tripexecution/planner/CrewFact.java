package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrewFact {
    private Integer id;
    private Integer familiarityLevel;
    private Integer licenseCategory;
    private Integer totalDutyMinutes;
}
