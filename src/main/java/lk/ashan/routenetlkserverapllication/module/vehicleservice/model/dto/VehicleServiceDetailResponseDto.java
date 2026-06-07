package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleServiceDetailResponseDto {
    private Integer id;
    private BranchSummaryDto branch;
    private String number;
    private VehicleSummaryDto vehicle;
    private VehicleServiceTypeDto vehicleservicetype;
    private IncidentSummaryDto incident;
    private VehicleServiceStatusDto vehicleservicestatus;
    private VehicleServicePriorityDto vehicleservicepriority;
    private LocalDate docreated;
    private List<VehicleServicePartDto> vehicleserviceparts;
}
