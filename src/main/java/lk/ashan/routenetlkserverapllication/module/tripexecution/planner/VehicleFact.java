package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleFact {
    private Integer id;
    private String busType;
    private int mileage;
//    private int nextServiceMilestone;
}
