package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleserviceschedule;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceScheduleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceSchedulerService {

    private final VehicleServiceRepository vehicleServiceRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleServiceStatusRepository statusRepository;
    private final VehicleServiceScheduleRepository scheduleRepository;

    public void scheduleServices() {

        List<Vehicleservice> services =
                vehicleServiceRepository.findByVehicleservicestatus_Name("Created");

        services.sort(this::comparePriority);

        Map<Integer, List<Vehicle>> vehiclesByBranch = loadAvailableVehicles();

        for (Vehicleservice service : services) {

            List<Vehicle> vehicles =
                    vehiclesByBranch.get(service.getBranch().getId());

            if (vehicles == null || vehicles.isEmpty()) continue;

            Vehicle vehicle = vehicles.get(0);

            LocalDate start = findNextAvailableDate(vehicle);

            int duration = resolveDuration(service);

            LocalDate end = start.plusDays(duration);

            // Create schedule record
            Vehicleserviceschedule schedule = new Vehicleserviceschedule();

            schedule.setVehicleservice(service);
            schedule.setDoscheduledstart(start);
            schedule.setDoscheduledend(end);

            scheduleRepository.save(schedule);

            // update service status
            service.setVehicleservicestatus(
                    statusRepository.findByName("SCHEDULED")
            );

            vehicleServiceRepository.save(service);
        }
    }

    private Map<Integer, List<Vehicle>> loadAvailableVehicles() {

        List<Vehicle> vehicles =
                vehicleRepository.findByVehiclestatus_Name("Available");

        return vehicles.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getBranch().getId()
                ));
    }

    private LocalDate findNextAvailableDate(Vehicle vehicle) {

        LocalDate lastEnd =
                scheduleRepository.findLastScheduledDate(vehicle.getId());

        if (lastEnd == null) {
            return LocalDate.now();
        }

        return lastEnd.plusDays(1);
    }

    private int resolveDuration(Vehicleservice service) {

        String type = service.getVehicleservicetype().getName().toUpperCase();

        return switch (type) {
            case "PRE TRIP" -> 1;
            case "PREVENTIVE" -> 2;
            case "POST INCIDENT" -> 3;
            default -> 1;
        };
    }

    private int comparePriority(Vehicleservice a, Vehicleservice b) {

        return priorityScore(b) - priorityScore(a);
    }

    private int priorityScore(Vehicleservice service) {

        String p = service.getVehicleservicepriority().getName();

        return switch (p) {
            case "CRITICAL" -> 3;
            case "HIGH" -> 2;
            default -> 1;
        };
    }



}
