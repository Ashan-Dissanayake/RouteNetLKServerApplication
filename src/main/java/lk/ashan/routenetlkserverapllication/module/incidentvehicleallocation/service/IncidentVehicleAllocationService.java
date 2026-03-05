package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incidentstatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentStatusRepository;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentState;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper.IncidentVehicleAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocationstatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationStatusRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.AllocationStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.IncidentVehicleAllocationState;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.IncidentVehicleAllocationStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationContext;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationValidationExecutor;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
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
    private final IncidentStatusRepository incidentStatusRepository;

    private final IncidentVehicleAllocationMapper incidentVehicleAllocationMapper;
    private final AllocationValidationExecutor validationExecutor;
    private final IncidentVehicleAllocationStatusFactory incidentVehicleAllocationStatusFactory;
    private final IncidentStatusFactory incidentStatusFactory;
    private final AllocationStateTransitionHandler allocationStateTransitionHandler;



    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> getIncidentVehicleAllocations() {
        return incidentVehicleAllocationMapper.toDtoList(incidentVehicleAllocationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> searchIncidentAllocations(@NotNull HashMap<String, String> params) {

        List<Incidentvehicleallocation> incidentVehicleAllocations = incidentVehicleAllocationRepository.findAll();

        if (!params.isEmpty()) {

            String incidentId = params.get("ssincident");
            String doReleased = params.get("ssdoreleased");

            Stream<Incidentvehicleallocation> incidentvehicleallocationStream = incidentVehicleAllocations.stream();

            if (incidentId != null)
                incidentvehicleallocationStream = incidentvehicleallocationStream.filter(t -> t.getIncident().getId() == Integer.parseInt(incidentId));
            if (doReleased != null)
                incidentvehicleallocationStream = incidentvehicleallocationStream.filter(t -> t.getDoreleased() == LocalDateTime.parse(doReleased));

            return incidentVehicleAllocationMapper.toDtoList(incidentvehicleallocationStream.collect(Collectors.toList()));
        }

        return incidentVehicleAllocationMapper.toDtoList(incidentVehicleAllocations);
    }

    public IncidentVehicleAllocationDetailsResponseDto createAllocation(
            IncidentVehicleAllocationCreateRequestDto request
    ) {
        Incident incident = incidentRepository.findById(request.getIncident().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Branch branch = branchRepository.findById(request.getProvidebranch().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        AllocationContext context = AllocationContext.builder()
                .incident(incident)
                .vehicle(vehicle)
                .providingBranch(branch)
                .build();

        validationExecutor.validate(context);

        Incidentvehicleallocation allocation = incidentVehicleAllocationMapper.toEntity(request);

        IncidentVehicleAllocationState state = incidentVehicleAllocationStatusFactory
                .getState(request.getIncidentvehicleallocationstatus().getName());
        state.validateInitial();

        Incidentvehicleallocationstatus incidentvehicleallocationstatus = incidentVehicleAllocationStatusRepository
                .findByName(request.getIncidentvehicleallocationstatus().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        allocation.setIncidentvehicleallocationstatus(incidentvehicleallocationstatus);

        Incidentvehicleallocation savedIncidentVehicleAllocation = incidentVehicleAllocationRepository.save(allocation);

        return incidentVehicleAllocationMapper.toDto(savedIncidentVehicleAllocation);

    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto startHandling(@NotNull Integer allocationId) {

        Incidentvehicleallocation allocation = incidentVehicleAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        Incidentvehicleallocationstatus inProgress = incidentVehicleAllocationStatusRepository
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

            Incidentstatus incidentstatus = incidentStatusRepository.findByName("IN PROGRESS")
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

            incidentState.transitionTo(incident,incidentstatus);
        }

        vehicleRepository.save(vehicle);
        incidentRepository.save(incident);
      Incidentvehicleallocation savedIncidentVehicleAllocation =  incidentVehicleAllocationRepository.save(allocation);
      return incidentVehicleAllocationMapper.toDto(savedIncidentVehicleAllocation);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto releaseAllocation(@NotNull Integer allocationId) {
        Incidentvehicleallocation allocation = incidentVehicleAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        Incidentvehicleallocationstatus releasedStatus = incidentVehicleAllocationStatusRepository
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

            Incidentstatus resolveStatus = incidentStatusRepository.findByName("Resolved")
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

            incidentState.transitionTo(incident,resolveStatus);
        }

        vehicleRepository.save(vehicle);
        incidentRepository.save(incident);
        Incidentvehicleallocation saved = incidentVehicleAllocationRepository.save(allocation);
        return incidentVehicleAllocationMapper.toDto(saved);

    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto cancelAllocation(@NotNull Integer allocationId) {

        Incidentvehicleallocation allocation = incidentVehicleAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        Incidentvehicleallocationstatus currentStatus = allocation.getIncidentvehicleallocationstatus();

        if (currentStatus.getName().equalsIgnoreCase( "RELEASED") || currentStatus.getName().equalsIgnoreCase("CANCELLED")) {
            throw new InvalidStateTransitionException("Cannot cancel allocation in state: " + currentStatus);
        }

        Incidentvehicleallocationstatus canceledStatus = incidentVehicleAllocationStatusRepository
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
                Incidentstatus resolveStatus = incidentStatusRepository.findByName("Resolved")
                        .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
                IncidentState incidentState = incidentStatusFactory.getState(incident.getIncidentstatus().getName());
                incidentState.transitionTo(incident,resolveStatus);
            }
        }

        vehicleRepository.save(vehicle);
        incidentRepository.save(incident);
        Incidentvehicleallocation saved = incidentVehicleAllocationRepository.save(allocation);

        return incidentVehicleAllocationMapper.toDto(saved);
    }



}
