package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteFact {
    private Integer id;
    private Integer requiredFamiliarityLevel;
    private double distanceKm;
}
