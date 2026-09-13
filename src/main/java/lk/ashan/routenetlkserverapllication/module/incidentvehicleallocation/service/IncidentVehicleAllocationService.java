package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import jakarta.persistence.criteria.Predicate;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides business logic for creating, searching, and updating vehicle allocations
 * associated with incidents.
 */
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

    /**
     * Retrieves all incident vehicle allocations currently available in the data store.
     *
     * @return a list of {@link IncidentVehicleAllocationDetailsResponseDto} objects for all allocations.
     */
    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> getIncidentVehicleAllocations() {
        return incidentVehicleAllocationMapper.toDtoList(incidentVehicleAllocationRepository.findAll());
    }

    /**
     * Searches for incident vehicle allocations using the provided filter parameters.
     *
     * @param params a map of filter values, including "ssvehicle" for vehicle ID and
     *               "ssdoreleased" for the release date/time.
     * @return a list of {@link IncidentVehicleAllocationDetailsResponseDto} objects that match the filter.
     * @throws IllegalArgumentException if a filter value cannot be parsed into a valid integer or date-time value.
     * @throws NullPointerException if the provided filter map is {@code null}.
     */
    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto> searchIncidentAllocations(
            @NotNull HashMap<String, String> params) {

        Specification<IncidentVehicleAllocation> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    String vehicleId = params.get("ssvehicle");
                    String doReleased = params.get("ssdoreleased");

                    if (vehicleId != null && !vehicleId.isBlank()) {
                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("vehicle").get("id"),
                                        Integer.parseInt(vehicleId)
                                )
                        );
                    }

                    if (doReleased != null && !doReleased.isBlank()) {
                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("doreleased"),
                                        LocalDateTime.parse(doReleased)
                                )
                        );
                    }

                    return criteriaBuilder.and(
                            predicates.toArray(new Predicate[0])
                    );
                };

        List<IncidentVehicleAllocation> allocations =
                incidentVehicleAllocationRepository.findAll(specification);

        return incidentVehicleAllocationMapper.toDtoList(allocations);
    }

    /**
     * Creates a new incident vehicle allocation based on the supplied request.
     *
     * @param request the allocation creation request containing the incident, vehicle,
     *                provided branch, and allocation status details.
     * @return the newly created allocation represented as a {@link IncidentVehicleAllocationDetailsResponseDto}.
     * @throws ResourceNotFoundException if the associated incident, vehicle, or branch does not exist.
     * @throws IllegalArgumentException if the allocation request fails domain validation.
     */
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

    /**
     * Moves an allocation to the in-progress state.
     *
     * @param id the ID of the allocation to update.
     * @return the updated allocation details as a {@link IncidentVehicleAllocationDetailsResponseDto}.
     * @throws ResourceNotFoundException if no allocation exists for the specified ID.
     */
    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto inProgress(@NotNull Integer id) {
        IncidentVehicleAllocationStatus status = incidentVehicleAllocationStatusService.getByName("In Progress");

        IncidentVehicleAllocation existing = getById(id);
        incidentVehicleAllocationStateTransitionHandler.transitionTo(existing, status);

        return incidentVehicleAllocationMapper.toDto(existing);
    }

    /**
     * Marks an allocation as released and records the release timestamp.
     *
     * @param id the ID of the allocation to release.
     * @return the updated allocation details as a {@link IncidentVehicleAllocationDetailsResponseDto}.
     * @throws ResourceNotFoundException if no allocation exists for the specified ID.
     */
    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto released(@NotNull Integer id) {
        IncidentVehicleAllocationStatus status = incidentVehicleAllocationStatusService.getByName("Released");

        IncidentVehicleAllocation existing = getById(id);
        existing.setDoreleased(LocalDateTime.now());
        incidentVehicleAllocationStateTransitionHandler.transitionTo(existing, status);

        return incidentVehicleAllocationMapper.toDto(existing);
    }

    /**
     * Cancels an existing incident vehicle allocation.
     *
     * @param id the ID of the allocation to cancel.
     * @return the updated allocation details as a {@link IncidentVehicleAllocationDetailsResponseDto}.
     * @throws ResourceNotFoundException if no allocation exists for the specified ID.
     */
    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto cancelled(@NotNull Integer id) {
        IncidentVehicleAllocationStatus status = incidentVehicleAllocationStatusService.getByName("Cancelled");

        IncidentVehicleAllocation existing = getById(id);
        incidentVehicleAllocationStateTransitionHandler.transitionTo(existing, status);

        return incidentVehicleAllocationMapper.toDto(existing);
    }

    /**
     * Retrieves an incident vehicle allocation by its ID.
     *
     * @param id the ID of the allocation to retrieve.
     * @return the {@link IncidentVehicleAllocation} entity corresponding to the given ID.
     * @throws ResourceNotFoundException if no allocation exists for the specified ID.
     */
    private IncidentVehicleAllocation getById(Integer id) {
        return incidentVehicleAllocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with id: " + id));
    }

}
