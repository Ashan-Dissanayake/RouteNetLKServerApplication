package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper.IncidentVehicleAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation.IncidentVehicleAllocationValidator;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
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

    private final IncidentVehicleAllocationRepository
            incidentVehicleAllocationRepository;

    private final IncidentVehicleAllocationStatusService
            incidentVehicleAllocationStatusService;

    private final IncidentRepository incidentRepository;
    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;

    private final IncidentVehicleAllocationMapper
            incidentVehicleAllocationMapper;

    private final IncidentVehicleAllocationValidator
            incidentVehicleAllocationValidator;

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto>
    getIncidentVehicleAllocations() {

        return incidentVehicleAllocationMapper.toDtoList(
                incidentVehicleAllocationRepository.findAll()
        );
    }

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationDetailsResponseDto>
    searchIncidentAllocations(
            @NotNull HashMap<String, String> params
    ) {

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
                incidentVehicleAllocationRepository.findAll(
                        specification
                );

        return incidentVehicleAllocationMapper.toDtoList(
                allocations
        );
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto createAllocation(
            IncidentVehicleAllocationCreateRequestDto request
    ) {

        Incident incident =
                incidentRepository.findById(
                                request.getIncident().getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Incident not found"
                                )
                        );

        Vehicle vehicle =
                vehicleRepository.findById(
                                request.getVehicle().getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vehicle not found"
                                )
                        );

        branchRepository.findById(
                        request.getProvidedbranch().getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Branch not found"
                        )
                );

        incidentVehicleAllocationValidator.validateCreate(
                incident,
                vehicle
        );

        IncidentVehicleAllocation allocation =
                incidentVehicleAllocationMapper.toEntity(request);

        IncidentVehicleAllocationStatus initialStatus =
                incidentVehicleAllocationStatusService.getByName(
                        request.getIncidentvehicleallocationstatus()
                                .getName()
                );

        incidentVehicleAllocationValidator.validateInitialStatus(
                initialStatus
        );

        allocation.setIncidentvehicleallocationstatus(
                initialStatus
        );

        allocation.setDoassigned(
                LocalDateTime.now()
        );

        IncidentVehicleAllocation saved =
                incidentVehicleAllocationRepository.save(
                        allocation
                );

        return incidentVehicleAllocationMapper.toDto(
                saved
        );
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto inProgress(
            @NotNull Integer id
    ) {

        IncidentVehicleAllocationStatus status =
                incidentVehicleAllocationStatusService.getByName(
                        "In Progress"
                );

        IncidentVehicleAllocation existing =
                getById(id);

        changeStatus(
                existing,
                status
        );

        return incidentVehicleAllocationMapper.toDto(
                existing
        );
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto released(
            @NotNull Integer id
    ) {

        IncidentVehicleAllocationStatus status =
                incidentVehicleAllocationStatusService.getByName(
                        "Released"
                );

        IncidentVehicleAllocation existing =
                getById(id);

        changeStatus(
                existing,
                status
        );

        existing.setDoreleased(
                LocalDateTime.now()
        );

        return incidentVehicleAllocationMapper.toDto(
                existing
        );
    }

    @Transactional
    public IncidentVehicleAllocationDetailsResponseDto cancelled(
            @NotNull Integer id
    ) {

        IncidentVehicleAllocationStatus status =
                incidentVehicleAllocationStatusService.getByName(
                        "Cancelled"
                );

        IncidentVehicleAllocation existing =
                getById(id);

        changeStatus(
                existing,
                status
        );

        return incidentVehicleAllocationMapper.toDto(
                existing
        );
    }

    private void changeStatus(
            IncidentVehicleAllocation allocation,
            IncidentVehicleAllocationStatus targetStatus
    ) {

        IncidentVehicleAllocationStatus currentStatus =
                allocation.getIncidentvehicleallocationstatus();

        incidentVehicleAllocationValidator.validateStatusTransition(
                currentStatus,
                targetStatus
        );

        allocation.setIncidentvehicleallocationstatus(
                targetStatus
        );
    }

    private IncidentVehicleAllocation getById(
            @NotNull Integer id
    ) {

        return incidentVehicleAllocationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Allocation not found with id: " + id
                        )
                );
    }
}