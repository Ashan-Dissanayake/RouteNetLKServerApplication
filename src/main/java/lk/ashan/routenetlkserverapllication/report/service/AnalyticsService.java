package lk.ashan.routenetlkserverapllication.report.service;

import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.report.model.dto.*;
import lk.ashan.routenetlkserverapllication.report.model.projection.*;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableUserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TripExecutionRepository tripExecutionRepository;
    private final FareCollectionRepository fareCollectionRepository;
    private final VehicleServiceRepository vehicleServiceRepository;
    private final IncidentRepository incidentRepository;

    @DisableUserFilter
    public Report1ResponseDto getFleetDispatchAndBreakdownMetrics() {
        // 1. Define standard order of days matching MySQL's WEEKDAY() (0 = Monday, 6 = Sunday)
        List<String> days =
                Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        // 2. Initialize fixed-size structures with 0s for all 7 days
        Integer[] successfulTripsArray = new Integer[7];
        Integer[] breakdownCountsArray = new Integer[7];
        Arrays.fill(successfulTripsArray, 0);
        Arrays.fill(breakdownCountsArray, 0);

        // 3. Fetch data from the 2 separate queries (3 = Completed, 1 = Breakdown)
        List<Object[]> tripsData = incidentRepository.getTripsCountByDay(3);
        List<Object[]> incidentsData = incidentRepository.getIncidentsCountByDay(1);

        // 4. Map trip counts into the correct day index positions
        for (Object[] row : tripsData) {
            int dayIdx = ((Number) row[0]).intValue();
            int count = ((Number) row[1]).intValue();
            successfulTripsArray[dayIdx] = count;
        }

        // 5. Map incident counts into the correct day index positions
        for (Object[] row : incidentsData) {
            int dayIdx = ((Number) row[0]).intValue();
            int count = ((Number) row[1]).intValue();
            breakdownCountsArray[dayIdx] = count;
        }

        // 6. Build and return the final DTO
        return Report1ResponseDto.builder()
                .days(days)
                .successfulTrips(Arrays.asList(successfulTripsArray))
                .breakdownCounts(Arrays.asList(breakdownCountsArray))
                .build();
    }

    @DisableUserFilter
    public Report2ResponseDto getDepotRevenueMetrics() {
        List<Report2Projection> records = fareCollectionRepository.getDepotFinancialReconciliationMetrics();

        List<String> depots = new ArrayList<>();
        List<BigDecimal> cashAmounts = new ArrayList<>();
        List<BigDecimal> digitalAmounts = new ArrayList<>();

        for (Report2Projection record : records) {
            depots.add(record.getDepotName());
            cashAmounts.add(record.getCashAmount() != null ? record.getCashAmount() : BigDecimal.ZERO);
            digitalAmounts.add(record.getDigitalAmount() != null ? record.getDigitalAmount() : BigDecimal.ZERO);
        }

        return Report2ResponseDto.builder()
                .depots(depots)
                .cashAmounts(cashAmounts)
                .digitalAmounts(digitalAmounts)
                .build();
    }


    @DisableUserFilter
    public Report3ResponseDto getMaintenanceTrendsMetrics() {
        List<Report3Projection> records = vehicleServiceRepository.getMaintenanceLifecycleMetrics();

        List<String> weeks = records.stream()
                .map(Report3Projection::getWeekLabel)
                .collect(Collectors.toList());

        List<Integer> completed = records.stream()
                .map(Report3Projection::getCompletedServices)
                .collect(Collectors.toList());

        List<Integer> pending = records.stream()
                .map(Report3Projection::getPendingBacklog)
                .collect(Collectors.toList());

        // Reverse to show oldest → latest
        Collections.reverse(weeks);
        Collections.reverse(completed);
        Collections.reverse(pending);

        return Report3ResponseDto.builder()
                .weeks(weeks)
                .completedServices(completed)
                .pendingBacklog(pending)
                .build();
    }

    @DisableUserFilter
    public Report4ResponseDto getDynamicPerformanceMetrics(Date start, Date end) {
        List<Report4Projection> records = tripExecutionRepository.getDynamicPerformanceMetrics(start, end);

        List<String> logDates = new ArrayList<>();
        List<Long> totalPassengers = new ArrayList<>();
        List<Double> totalDistances = new ArrayList<>();

        for (Report4Projection record : records) {
            logDates.add(record.getLogDate());
            totalPassengers.add(record.getTotalPassengers() != null ? record.getTotalPassengers() : 0L);
            totalDistances.add(record.getTotalDistance() != null ? record.getTotalDistance() : 0.0);
        }

        return Report4ResponseDto.builder()
                .logDates(logDates)
                .totalPassengers(totalPassengers)
                .totalDistances(totalDistances)
                .build();
    }

    @DisableUserFilter
    public Report5ResponseDto getIncidentDistributionMetrics() {
        List<Report5Projection> records = incidentRepository.getIncidentDistributionMetrics();

        List<String> types = records.stream().map(Report5Projection::getIncidentTypeName).collect(Collectors.toList());
        List<Long> counts = records.stream().map(Report5Projection::getIncidentCount).collect(Collectors.toList());

        return Report5ResponseDto.builder()
                .types(types)
                .counts(counts)
                .build();
    }

}

//    Integer[] dummysuccessfulTripsArray = {12, 15, 9, 20, 18, 10, 5};
//    Integer[] dummybreakdownCountsArray = {2, 1, 3, 0, 4, 2, 1};
