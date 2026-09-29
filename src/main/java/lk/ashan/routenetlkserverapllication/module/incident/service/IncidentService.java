package lk.ashan.routenetlkserverapllication.module.incident.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentTypeRepository;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentValidator;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service class for managing incidents.
 * Provides methods for creating, retrieving, and updating incidents,
 * as well as validating incident business rules.
 */
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final TripExecutionRepository tripExecutionRepository;
    private final IncidentTypeRepository incidentTypeRepository;
    private final IncidentStatusService incidentStatusService;
    private final IncidentMapper incidentMapper;
    private final IncidentValidator incidentValidator;

    /**
     * Retrieves all incidents.
     *
     * @return a list of {@link IncidentDetailResponseDto} containing details of all incidents.
     */
    @Transactional(readOnly = true)
    public List<IncidentDetailResponseDto> getIncidents() {
        return incidentMapper.toDtoList(
                incidentRepository.findAll()
        );
    }

    /**
     * Searches for incidents based on the provided parameters.
     *
     * @param params a map of search parameters.
     * @return a list of {@link IncidentDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<IncidentDetailResponseDto> searchIncidents(
            @NotNull HashMap<String, String> params) {

        Specification<Incident> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    String incidentTypeId =
                            params.get("ssincidenttype");

                    String doReport =
                            params.get("ssdoreport");

                    String tripExecutionId =
                            params.get("sstripexecution");

                    if (incidentTypeId != null
                            && !incidentTypeId.isBlank()) {

                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("incidenttype").get("id"),
                                        Integer.parseInt(incidentTypeId)
                                )
                        );
                    }

                    if (doReport != null
                            && !doReport.isBlank()) {

                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("doreported"),
                                        LocalDate.parse(doReport)
                                )
                        );
                    }

                    if (tripExecutionId != null
                            && !tripExecutionId.isBlank()) {

                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("tripexecution").get("id"),
                                        Integer.parseInt(tripExecutionId)
                                )
                        );
                    }

                    return criteriaBuilder.and(
                            predicates.toArray(new Predicate[0])
                    );
                };

        List<Incident> incidents =
                incidentRepository.findAll(specification);

        return incidentMapper.toDtoList(incidents);
    }

    /**
     * Creates a new incident.
     *
     * @param dto the {@link IncidentCreateRequestDto} containing incident creation details.
     * @return the created {@link IncidentDetailResponseDto}.
     * @throws ResourceNotFoundException if the trip execution or incident type is not found.
     * @throws BusinessRuleViolationException if the incident creation rules are violated.
     */
    @Transactional
    public IncidentDetailResponseDto create(
            IncidentCreateRequestDto dto) {

        TripExecution existingTripExecution =
                tripExecutionRepository.findById(
                                dto.getTripexecution().getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Trip Execution not found"
                                )
                        );

        incidentTypeRepository.findById(
                        dto.getIncidenttype().getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid incident type"
                        )
                );

        /*
         * Validate incident creation rules.
         *
         * This replaces:
         * - IncidentContext
         * - IncidentContextBuilder
         * - IncidentStrategy
         * - MechanicalIncidentStrategy
         * - TripTimeValidation
         */
        incidentValidator.validateCreate(
                dto,
                existingTripExecution
        );

        Incident incident =
                incidentMapper.toEntity(dto);

        IncidentStatus incidentStatus =
                incidentStatusService.getByName(
                        dto.getIncidentstatus().getName()
                );

        /*
         * Validate the initial incident status.
         *
         * The previous State Pattern used IncidentStateFactory
         * and IncidentState.validateInitial().
         */
        incidentValidator.validateInitialStatus(
                incidentStatus
        );

        incident.setIncidentstatus(incidentStatus);

        /*
         * Incident odometer is taken from the trip execution's
         * current/end odometer.
         */
        incident.setOdometeratincident(
                existingTripExecution.getEndodometer()
        );

        Incident saved =
                incidentRepository.save(incident);

        return incidentMapper.toDto(saved);
    }

    /**
     * Retrieves a summary of all incidents.
     *
     * @return a list of {@link IncidentSummaryDto} containing summary details of all incidents.
     */
    @Transactional(readOnly = true)
    public List<IncidentSummaryDto> getSummaryIncidents() {
        return incidentMapper.toSummaryDtoList(
                incidentRepository.findAll()
        );
    }

    /**
     * Marks an incident as "In Progress".
     *
     * @param incidentId the ID of the incident to update.
     * @return the updated {@link IncidentDetailResponseDto}.
     * @throws ResourceNotFoundException if the incident is not found.
     * @throws BusinessRuleViolationException if the status transition is invalid.
     */
    @Transactional
    public IncidentDetailResponseDto inProgress(
            @NotNull Integer incidentId) {

        IncidentStatus inProgressStatus =
                incidentStatusService.getByName(
                        "In Progress"
                );

        Incident existing =
                getById(incidentId);

        changeStatus(
                existing,
                inProgressStatus
        );

        return incidentMapper.toDto(existing);
    }

    /**
     * Marks an incident as "Vehicle Recovery".
     *
     * @param incidentId the ID of the incident to update.
     * @return the updated {@link IncidentDetailResponseDto}.
     * @throws ResourceNotFoundException if the incident is not found.
     * @throws BusinessRuleViolationException if the incident type or
     * status transition is invalid.
     */
    @Transactional
    public IncidentDetailResponseDto vehicleRecovery(
            @NotNull Integer incidentId) {

        IncidentStatus recoveryStatus =
                incidentStatusService.getByName(
                        "Vehicle Recovery"
                );

        Incident existing =
                getById(incidentId);

        incidentValidator.validateVehicleRecovery(
                existing
        );

        changeStatus(
                existing,
                recoveryStatus
        );

        return incidentMapper.toDto(existing);
    }

    /**
     * Marks an incident as "Pending Allocation".
     *
     * @param incidentId the ID of the incident to update.
     * @return the updated {@link IncidentDetailResponseDto}.
     * @throws ResourceNotFoundException if the incident is not found.
     * @throws BusinessRuleViolationException if the incident type or
     * status transition is invalid.
     */
    @Transactional
    public IncidentDetailResponseDto pendingAllocation(
            @NotNull Integer incidentId) {

        IncidentStatus pendingStatus =
                incidentStatusService.getByName(
                        "Pending Allocation"
                );

        Incident existing =
                getById(incidentId);

        incidentValidator.validatePendingAllocation(
                existing
        );

        changeStatus(
                existing,
                pendingStatus
        );

        return incidentMapper.toDto(existing);
    }

    /**
     * Marks an incident as "Resolved".
     *
     * @param incidentId the ID of the incident to update.
     * @return the updated {@link IncidentDetailResponseDto}.
     * @throws ResourceNotFoundException if the incident is not found.
     * @throws BusinessRuleViolationException if the status transition is invalid.
     */
    @Transactional
    public IncidentDetailResponseDto resolved(
            @NotNull Integer incidentId) {

        IncidentStatus resolvedStatus =
                incidentStatusService.getByName(
                        "Resolved"
                );

        Incident existing =
                getById(incidentId);

        changeStatus(
                existing,
                resolvedStatus
        );

        return incidentMapper.toDto(existing);
    }

    /**
     * Marks an incident as "Closed".
     *
     * @param incidentId the ID of the incident to update.
     * @return the updated {@link IncidentDetailResponseDto}.
     * @throws ResourceNotFoundException if the incident is not found.
     * @throws BusinessRuleViolationException if the status transition is invalid.
     */
    @Transactional
    public IncidentDetailResponseDto closed(
            @NotNull Integer incidentId) {

        IncidentStatus closedStatus =
                incidentStatusService.getByName(
                        "Closed"
                );

        Incident existing =
                getById(incidentId);

        changeStatus(
                existing,
                closedStatus
        );

        return incidentMapper.toDto(existing);
    }

    /**
     * Validates and applies an incident status transition.
     *
     * @param incident the incident to update
     * @param targetStatus the requested target status
     */
    private void changeStatus(
            Incident incident,
            IncidentStatus targetStatus) {

        IncidentStatus currentStatus =
                incident.getIncidentstatus();

        incidentValidator.validateStatusTransition(
                currentStatus,
                targetStatus
        );

        incident.setIncidentstatus(
                targetStatus
        );
    }

    /**
     * Retrieves an incident by its ID.
     *
     * @param id the ID of the incident to retrieve.
     * @return the {@link Incident} entity.
     * @throws ResourceNotFoundException if the incident is not found.
     */
    private Incident getById(
            @NotNull Integer id) {

        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Incident not found"
                        )
                );
    }
}