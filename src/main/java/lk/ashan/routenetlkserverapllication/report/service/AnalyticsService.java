package lk.ashan.routenetlkserverapllication.report.service;

import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.report.model.dto.ChartDataDTO;
import lk.ashan.routenetlkserverapllication.report.model.projection.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TripExecutionRepository tripExecutionRepository;
    private final FareCollectionRepository fareCollectionRepository;
    private final VehicleServiceRepository vehicleServiceRepository;
    private final IncidentRepository incidentRepository;

    public ChartDataDTO getReport1Metrics() {
        List<Report1Projection> records = incidentRepository.getFleetDispatchAndBreakdownMetrics();
        return ChartDataDTO.builder()
                .labels(records.stream().map(Report1Projection::getDayName).collect(Collectors.toList()))
                .datasets(List.of(
                        new ChartDataDTO.DatasetDTO("Successful Trip Executions", records.stream().map(Report1Projection::getSuccessfulTrips).collect(Collectors.toList())),
                        new ChartDataDTO.DatasetDTO("Logged Breakdown Incidents", records.stream().map(Report1Projection::getBreakdownCount).collect(Collectors.toList()))
                )).build();
    }

    public ChartDataDTO getReport2Metrics() {
        List<Report2Projection> records = fareCollectionRepository.getDepotFinancialReconciliationMetrics();
        return ChartDataDTO.builder()
                .labels(records.stream().map(Report2Projection::getDepotName).collect(Collectors.toList()))
                .datasets(List.of(
                        new ChartDataDTO.DatasetDTO("Physical Cash Vault (LKR)", records.stream().map(Report2Projection::getCashAmount).collect(Collectors.toList())),
                        new ChartDataDTO.DatasetDTO("Digital ETM Validations (LKR)", records.stream().map(Report2Projection::getDigitalAmount).collect(Collectors.toList()))
                )).build();
    }

    public ChartDataDTO getReport3Metrics() {
        List<Report3Projection> records = vehicleServiceRepository.getMaintenanceLifecycleMetrics();
        var labels = records.stream().map(Report3Projection::getWeekLabel).collect(Collectors.toList());
        var completed = records.stream().map(Report3Projection::getCompletedServices).collect(Collectors.toList());
        var pending = records.stream().map(Report3Projection::getPendingBacklog).collect(Collectors.toList());

        Collections.reverse(labels);
        Collections.reverse(completed);
        Collections.reverse(pending);

        return ChartDataDTO.builder().labels(labels).datasets(List.of(
                new ChartDataDTO.DatasetDTO("Completed Vehicle Services", completed),
                new ChartDataDTO.DatasetDTO("Pending Maintenance Backlog", pending)
        )).build();
    }

    public ChartDataDTO getReport4Metrics(Date start, Date end) {
        List<Report4Projection> records = tripExecutionRepository.getDynamicPerformanceMetrics(start, end);
        return ChartDataDTO.builder()
                .labels(records.stream().map(Report4Projection::getLogDate).collect(Collectors.toList()))
                .datasets(List.of(
                        new ChartDataDTO.DatasetDTO("Aggregated Passenger Count", records.stream().map(Report4Projection::getTotalPassengers).collect(Collectors.toList())),
                        new ChartDataDTO.DatasetDTO("Distance Traveled (KM)", records.stream().map(Report4Projection::getTotalDistance).collect(Collectors.toList()))
                )).build();
    }

    public ChartDataDTO getReport5Metrics() {
        List<Report5Projection> records = incidentRepository.getIncidentDistributionMetrics();
        return ChartDataDTO.builder()
                .labels(records.stream().map(Report5Projection::getIncidentTypeName).collect(Collectors.toList()))
                .datasets(List.of(
                        new ChartDataDTO.DatasetDTO("Incidents Proportion", records.stream().map(Report5Projection::getIncidentCount).collect(Collectors.toList()))
                )).build();
    }

}
