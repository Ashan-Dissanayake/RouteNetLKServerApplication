package lk.ashan.routenetlkserverapllication.module.trip.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleFact {

    private Integer id;
    private String number;
    private String status; // AVAILABLE, MAINTENANCE, BREAKDOWN
    private Integer depotId;
}
