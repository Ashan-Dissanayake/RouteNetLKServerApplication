package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper.IncidentVehicleAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.IncidentVehicleAllocationStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state.IncidentVehicleAllocationStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationContext;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationContextBuilder;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.AllocationValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IncidentVehicleAllocationService {

    private final IncidentVehicleAllocationRepository incidentVehicleAllocationRepository;
    private final IncidentVehicleAllocationStatusService incidentVehicleAllocationStatusService;
    private final IncidentRepository incidentRepository;
    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;

    private final IncidentVehicleAllocationMapper incidentVehicleAllocationMapper;
    private final IncidentVehicleAllocationStateTransitionHandler incidentVehicleAllocationStateTransitionHandler;
    private final IncidentVehicleAllocationStatusFactory incidentVehicleAllocationStatusFactory;
    private final AllocationContextBuilder allocationContextBuilder;
    private final List<AllocationValidationStrategy> allocationValidationStrategies;

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> getIncidentVehicleAllocations() {
        return incidentVehicleAllocationMapper.toDtoList(incidentVehicleAllocationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> searchIncidentAllocations(@NotNull HashMap<String, String> params) {

        String vehicleId = params.get("ssvehicle)");
        String doReleased = params.get("ssdoreleased");

        Stream<IncidentVehicleAllocation> incidentvehicleallocationStream = incidentVehicleAllocationRepository.findAll().stream();

        if (vehicleId != null)
            incidentvehicleallocationStream = incidentvehicleallocationStream.filter(t -> t.getVehicle().getId() == Integer.parseInt(vehicleId));
        if (doReleased != null)
            incidentvehicleallocationStream = incidentvehicleallocationStream.filter(t -> t.getDoreleased() == LocalDateTime.parse(doReleased));

        return incidentVehicleAllocationMapper.toDtoList(incidentvehicleallocationStream.collect(Collectors.toList()));
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto createAllocation(
            IncidentVehicleAllocationCreateRequestDto request
    ) {
        incidentRepository.findById(request.getIncident().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        vehicleRepository.findById(request.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        branchRepository.findById(request.getProvidedbranch().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        AllocationContext context = allocationContextBuilder.buildForCreate(request);
        allocationValidationStrategies.forEach(strategy -> strategy.validate(context));

        IncidentVehicleAllocation allocation = incidentVehicleAllocationMapper.toEntity(request);

        IncidentVehicleAllocationStatus initialStatus =
                incidentVehicleAllocationStatusService.
                        getByName(request.getIncidentvehicleallocationstatus().getName());

        incidentVehicleAllocationStatusFactory.getState(initialStatus.getName())
                .validateInitial();

        allocation.setIncidentvehicleallocationstatus(initialStatus);
        allocation.setDoassigned(LocalDateTime.now());
        IncidentVehicleAllocation savedIncidentVehicleAllocation = incidentVehicleAllocationRepository.save(allocation);

        return incidentVehicleAllocationMapper.toDto(savedIncidentVehicleAllocation);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto inProgress(@NotNull Integer id) {
        IncidentVehicleAllocationStatus status = incidentVehicleAllocationStatusService.getByName("In Progress");

        IncidentVehicleAllocation existing = getById(id);
        incidentVehicleAllocationStateTransitionHandler.transitionTo(existing, status);

        return incidentVehicleAllocationMapper.toDto(existing);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto released(@NotNull Integer id) {
        IncidentVehicleAllocationStatus status = incidentVehicleAllocationStatusService.getByName("Released");

        IncidentVehicleAllocation existing = getById(id);
        existing.setDoreleased(LocalDateTime.now());
        incidentVehicleAllocationStateTransitionHandler.transitionTo(existing, status);

        return incidentVehicleAllocationMapper.toDto(existing);
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto cancelled(@NotNull Integer id) {
        IncidentVehicleAllocationStatus status = incidentVehicleAllocationStatusService.getByName("Cancelled");

        IncidentVehicleAllocation existing = getById(id);
        incidentVehicleAllocationStateTransitionHandler.transitionTo(existing, status);

        return incidentVehicleAllocationMapper.toDto(existing);
    }

    private IncidentVehicleAllocation getById(Integer id) {
        return incidentVehicleAllocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with id: " + id));
    }

}
