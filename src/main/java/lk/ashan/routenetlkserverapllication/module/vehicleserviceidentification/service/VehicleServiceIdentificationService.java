package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicepriority;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicetype;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServicePriorityRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceStatusRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation.VehicleServiceContext;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation.VehicleServiceEvaluationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceIdentificationService {

    private final VehicleRepository vehicleRepository;
    private final VehicleServiceRepository vehicleServiceRepository;
    private final VehicleServicePriorityRepository priorityRepository;
    private final VehicleServiceTypeRepository serviceTypeRepository;
    private final VehicleServiceStatusRepository statusRepository;
    private final IncidentRepository incidentRepository;

    private final List<VehicleServiceEvaluationStrategy> strategies;

    private static final Set<String> ELIGIBLE_STATUSES =
            Set.of("AVAILABLE", "IN SERVICE", "RESERVED");

    @Transactional
    public void evaluateVehicles() {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        for (Vehicle vehicle : vehicles) {
            String status = vehicle.getVehiclestatus().getName().toUpperCase();

            if (!ELIGIBLE_STATUSES.contains(status)) continue;

            if (hasOpenService(vehicle)) continue;

            VehicleServiceContext context = buildContext(vehicle);

            for (VehicleServiceEvaluationStrategy strategy : strategies) {
                if (strategy.isServiceRequired(context)) {
                    createVehicleService(vehicle, strategy);
                    break;
                }
            }
        }
    }

    private VehicleServiceContext buildContext(Vehicle vehicle) {

        Incident incident =
                incidentRepository.findLatestIncidentByTrip_Permite_Vehicle_Id(vehicle.getId())
                        .orElse(null);

        boolean preTripRequired = vehicle.getVehiclestatus()
                .getName()
                .equalsIgnoreCase("AVAILABLE");

        return new VehicleServiceContext(
                vehicle,
                incident,
                vehicle.getMileage(),
                null,
                preTripRequired
        );
    }

    private void createVehicleService(Vehicle vehicle,
                                      VehicleServiceEvaluationStrategy strategy) {

        Vehicleservice service = new Vehicleservice();

        service.setVehicle(vehicle);
        service.setBranch(vehicle.getBranch());

        Vehicleservicetype type =
                serviceTypeRepository.getReferenceById(
                        strategy.getServiceTypeId()
                );

        service.setVehicleservicetype(type);

        Vehicleservicepriority priority =
                resolvePriority(type.getName());

        service.setVehicleservicepriority(priority);

        service.setDocreated(LocalDate.now());

        service.setDosuggestedstart(LocalDate.now());
        service.setDosuggestedend(LocalDate.now().plusDays(5));

        Vehicleservicestatus initialStatus =
                statusRepository.findByName("Available");

        service.setVehicleservicestatus(initialStatus);

        vehicleServiceRepository.save(service);

        log.info("Vehicle service created for vehicle {} type {}",
                vehicle.getNumber(),
                type.getName());
    }

    private Vehicleservicepriority resolvePriority(String serviceType) {

        return switch (serviceType.toUpperCase()) {
            case "POST_INCIDENT" -> priorityRepository.findByName("CRITICAL");
            case "PREVENTIVE" -> priorityRepository.findByName("HIGH");
            default -> priorityRepository.findByName("NORMAL");
        };
    }

    private boolean hasOpenService(Vehicle vehicle) {
        return vehicleServiceRepository.existsOpenService(vehicle.getId());
    }
}
