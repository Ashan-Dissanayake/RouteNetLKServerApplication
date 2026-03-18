package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentStatusService;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentState;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper.IncidentVehicleAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationStatusRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.AllocationStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.IncidentVehicleAllocationState;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.IncidentVehicleAllocationStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationContext;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationContextBuilder;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IncidentVehicleAllocationService {

    private final IncidentVehicleAllocationRepository incidentVehicleAllocationRepository;
    private final IncidentVehicleAllocationStatusRepository incidentVehicleAllocationStatusRepository;
    private final IncidentRepository incidentRepository;
    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;
    private final IncidentStatusService incidentStatusService;

    private final IncidentVehicleAllocationMapper incidentVehicleAllocationMapper;
    private final IncidentVehicleAllocationStatusFactory incidentVehicleAllocationStatusFactory;
    private final IncidentStatusFactory incidentStatusFactory;
    private final AllocationStateTransitionHandler allocationStateTransitionHandler;
    private final AllocationContextBuilder allocationContextBuilder;
    private final List<AllocationValidationStrategy> allocationValidationStrategies;


    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> getIncidentVehicleAllocations() {
        return incidentVehicleAllocationMapper.toDtoList(incidentVehicleAllocationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> searchIncidentAllocations(@NotNull HashMap<String, String> params) {

        List<IncidentVehicleAllocation> incidentVehicleAllocations = incidentVehicleAllocationRepository.findAll();

        if (!params.isEmpty()) {

            String incidentId = params.get("ssincident");
            String doReleased = params.get("ssdoreleased");

            Stream<IncidentVehicleAllocation> incidentvehicleallocationStream = incidentVehicleAllocations.stream();

            if (incidentId != null)
                incidentvehicleallocationStream = incidentvehicleallocationStream.filter(t -> t.getIncident().getId() == Integer.parseInt(incidentId));
            if (doReleased != null)
                incidentvehicleallocationStream = incidentvehicleallocationStream.filter(t -> t.getDoreleased() == LocalDateTime.parse(doReleased));

            return incidentVehicleAllocationMapper.toDtoList(incidentvehicleallocationStream.collect(Collectors.toList()));
        }

        return incidentVehicleAllocationMapper.toDtoList(incidentVehicleAllocations);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto createAllocation(
            IncidentVehicleAllocationCreateRequestDto request
    ) {
         incidentRepository.findById(request.getIncident().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        vehicleRepository.findById(request.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        branchRepository.findById(request.getProvidebranch().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        AllocationContext context = allocationContextBuilder.buildForCreate(request);
        allocationValidationStrategies.forEach(strategy -> strategy.validate(context));

        IncidentVehicleAllocation allocation = incidentVehicleAllocationMapper.toEntity(request);

        IncidentVehicleAllocationState state = incidentVehicleAllocationStatusFactory
                .getState(request.getIncidentvehicleallocationstatus().getName());
        state.validateInitial();

        IncidentVehicleAllocationStatus incidentvehicleallocationstatus = incidentVehicleAllocationStatusRepository
                .findByName(request.getIncidentvehicleallocationstatus().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        allocation.setIncidentvehicleallocationstatus(incidentvehicleallocationstatus);

        IncidentVehicleAllocation savedIncidentVehicleAllocation = incidentVehicleAllocationRepository.save(allocation);

        return incidentVehicleAllocationMapper.toDto(savedIncidentVehicleAllocation);

    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto startHandling(@NotNull Integer allocationId) {

        IncidentVehicleAllocation allocation = incidentVehicleAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        IncidentVehicleAllocationStatus inProgress = incidentVehicleAllocationStatusRepository
                .findByName("In progress")
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        allocationStateTransitionHandler.transitionTo(allocation, inProgress);

        Vehicle vehicle = vehicleRepository.findById(allocation.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (!vehicle.getVehiclestatus().getName().equalsIgnoreCase("AVAILABLE")) {
            throw new BusinessRuleViolationException("Vehicle is not available to start handling");
        }

        //step-x need to update vehicle status using vehicle status transition handler.
        //currently its with old architecture so need to refine the status and its transiton with handler

        Incident incident = allocation.getIncident();

        if (!incident.getIncidentstatus().getName().equalsIgnoreCase("IN PROGRESS")) {

            IncidentState incidentState = incidentStatusFactory
                    .getState(incident.getIncidentstatus().getName());

            IncidentStatus incidentstatus = incidentStatusService.getByName("In progress");
            incidentState.transitionTo(incident,incidentstatus);
        }

      vehicleRepository.save(vehicle);
      incidentRepository.save(incident);
      IncidentVehicleAllocation savedIncidentVehicleAllocation =  incidentVehicleAllocationRepository.save(allocation);
      return incidentVehicleAllocationMapper.toDto(savedIncidentVehicleAllocation);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto releaseAllocation(@NotNull Integer allocationId) {
        IncidentVehicleAllocation allocation = incidentVehicleAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        IncidentVehicleAllocationStatus releasedStatus = incidentVehicleAllocationStatusRepository
                .findByName("Released")
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        allocationStateTransitionHandler.transitionTo(
                allocation,releasedStatus
        );

        Vehicle vehicle = vehicleRepository.findById(allocation.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));


        //step-x need to update vehicle status using vehicle status transition handler.
        //currently its with old architecture so need to refine the status and its transiton with handler

        Incident incident = allocation.getIncident();

        boolean allReleased = incidentVehicleAllocationRepository
                .findByIncident_Id(incident.getId())
                .stream()
                .allMatch(a -> Objects.equals(a.getIncidentvehicleallocationstatus().getName(), "Released"));

        if (allReleased && !incident.getIncidentstatus().getName().equals("Resolved")) {

            IncidentState incidentState = incidentStatusFactory
                    .getState(incident.getIncidentstatus().getName());

            IncidentStatus resolveStatus = incidentStatusService.getByName("Resolved");
            incidentState.transitionTo(incident,resolveStatus);
        }

        vehicleRepository.save(vehicle);
        incidentRepository.save(incident);
        IncidentVehicleAllocation saved = incidentVehicleAllocationRepository.save(allocation);
        return incidentVehicleAllocationMapper.toDto(saved);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto cancelAllocation(@NotNull Integer allocationId) {

        IncidentVehicleAllocation allocation = incidentVehicleAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        IncidentVehicleAllocationStatus currentStatus = allocation.getIncidentvehicleallocationstatus();

        if (currentStatus.getName().equalsIgnoreCase( "RELEASED") || currentStatus.getName().equalsIgnoreCase("CANCELLED")) {
            throw new InvalidStateTransitionException("Cannot cancel allocation in state: " + currentStatus);
        }

        IncidentVehicleAllocationStatus canceledStatus = incidentVehicleAllocationStatusRepository
                .findByName("Cancelled")
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        allocationStateTransitionHandler.transitionTo(allocation, canceledStatus);

        Vehicle vehicle = vehicleRepository.findById(allocation.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
//
//        if (currentStatus.getName().equalsIgnoreCase("IN PROGRESS")) {
//            vehicleStateTransitionHandler.transition(
//                    vehicle,
//                    OperationalStatus.AVAILABLE
//            );
//        }

        Incident incident = allocation.getIncident();

        boolean activeAllocationsExist = incidentVehicleAllocationRepository
                .findByIncident_Id(incident.getId())
                .stream()
                .anyMatch(a -> a.getIncidentvehicleallocationstatus().getName().equalsIgnoreCase("ASSIGNED")
                        || a.getIncidentvehicleallocationstatus().getName().equalsIgnoreCase("IN PROGRESS"));

        if (!activeAllocationsExist) {
            // No more active allocations → incident may resolve
            if (!incident.getIncidentstatus().getName().equalsIgnoreCase("RESOLVED")) {
                IncidentStatus resolveStatus = incidentStatusService.getByName("Resolved");
                IncidentState incidentState = incidentStatusFactory.getState(incident.getIncidentstatus().getName());
                incidentState.transitionTo(incident,resolveStatus);
            }
        }

        vehicleRepository.save(vehicle);
        incidentRepository.save(incident);
        IncidentVehicleAllocation saved = incidentVehicleAllocationRepository.save(allocation);

        return incidentVehicleAllocationMapper.toDto(saved);
    }


}
