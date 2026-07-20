package lk.ashan.routenetlkserverapllication.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveIncidentDto {
    private String routeNumber;
    private String vehicleNumber;
    private String issueDescription;
    private String status;
}
