package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicepriority;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicetype;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServicePriorityRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceStatusRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation.PostIncidentServiceStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation.VehicleServiceContext;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation.VehicleServiceEvaluationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    // Eligible vehicle statuses
    private static final Set<String> ELIGIBLE_STATUSES =
            Set.of("AVAILABLE", "IN SERVICE", "RESERVED");

    @Transactional
    public void evaluateVehicles() {
        // Fetch only vehicles with eligible statuses
        List<Vehicle> vehicles = vehicleRepository.findByVehiclestatus_NameIn(ELIGIBLE_STATUSES);

        List<Integer> vehiclesWithOpenServices = vehicleServiceRepository.findVehicleIdsWithOpenServices();

        for ( Vehicle vehicle : vehicles){
            if (vehiclesWithOpenServices.contains(vehicle.getId())) continue;

            VehicleServiceContext context = buildContext(vehicle);

            for (VehicleServiceEvaluationStrategy strategy : strategies) {
                if (strategy.isServiceRequired(context)) {

                    // Prevent duplicate PostIncident services
                    if (strategy instanceof PostIncidentServiceStrategy &&
                            vehicleServiceRepository.existsOpenServiceForIncident(context.getIncident().getId())) {
                        continue;
                    }

                    createVehicleService(vehicle, strategy, context);
                    break;
                }
            }

        }
    }

    private VehicleServiceContext buildContext(Vehicle vehicle) {

        Incident incident =
                incidentRepository.findLatestIncidentByTrip_Permite_Vehicle_Id(vehicle.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Incident not found with vehicle " + vehicle.getNumber()));

        boolean preTripRequired = ELIGIBLE_STATUSES.contains(vehicle.getVehiclestatus().getName());

        return VehicleServiceContext.builder()
                .vehicle(vehicle)
                .incident(incident)
                .mileage(vehicle.getMileage())
                .preTripRequired(preTripRequired)
                .build();
    }

    private void createVehicleService(Vehicle vehicle,
                                      VehicleServiceEvaluationStrategy strategy,
                                      VehicleServiceContext context) {

        Vehicleservice service = new Vehicleservice();

        service.setVehicle(vehicle);
        service.setBranch(vehicle.getBranch());

        Vehicleservicetype type =
                serviceTypeRepository.getReferenceById(strategy.getServiceTypeId());

        service.setVehicleservicetype(type);

        Vehicleservicepriority priority = resolvePriority(type.getName());
        service.setVehicleservicepriority(priority);

        Vehicleservicestatus initialStatus = statusRepository.findByName("Created");
        service.setVehicleservicestatus(initialStatus);

        service.setNumber(generateServiceNumber());

        service.setDocreated(LocalDate.now());

        if (strategy instanceof PostIncidentServiceStrategy) {
            service.setIncident(context.getIncident());
        }

        vehicleServiceRepository.save(service);

        log.info("Vehicle service created: {} | Vehicle: {} | Type: {} | Priority: {}",
                service.getNumber(),
                vehicle.getNumber(),
                type.getName(),
                priority.getName());
    }

    private Vehicleservicepriority resolvePriority(String serviceType) {
        return switch (serviceType.toUpperCase()) {
            case "POST_INCIDENT" -> priorityRepository.findByName("Critical");
            case "PREVENTIVE" -> priorityRepository.findByName("High");
            default -> priorityRepository.findByName("Normal");
        };
    }

    private String generateServiceNumber() {
        long count = vehicleServiceRepository.countByDate(LocalDate.now()) + 1;
        return String.format("VS-%s-%04d", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), count);
    }
}
