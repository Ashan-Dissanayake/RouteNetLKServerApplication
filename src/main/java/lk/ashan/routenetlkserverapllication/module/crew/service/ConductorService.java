package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.ConductorMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.ConductorContextBuilder;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.ConductorValidationContext;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.ConductorValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing conductors.
 * Provides methods for retrieving, creating, and updating conductor data.
 */
@Service
@RequiredArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;
    private final NumberGeneratorService numberGeneratorService;
    private final CrewStatusService crewStatusService;
    private final RouteFamiliarityLevelService routeFamiliarityLevelService;

    private final List<ConductorValidationStrategy> validationStrategies;
    private final ConductorContextBuilder conductorContextBuilder;

    /**
     * Retrieves all conductors.
     *
     * @return a list of {@link ConductorDetailResponseDto} containing details of all conductors.
     */
    @Transactional(readOnly = true)
    public List<ConductorDetailResponseDto> getConductors() {
        return conductorMapper.toDtoList(conductorRepository.findAll());
    }

    /**
     * Searches for conductors based on the provided parameters.
     *
     * @param params a map of search parameters, including "ssnumber", "sscrewstatus", and "ssroutefamilitylevel".
     * @return a list of {@link ConductorDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<ConductorDetailResponseDto> searchConductor(@NotNull HashMap<String, String> params) {

        String number = params.get("ssnumber");
        String crewStatusId = params.get("sscrewstatus");
        String routeFamiliarityLevelId = params.get("ssroutefamilitylevel");

        Stream<Conductor> conductorStream = conductorRepository.findAll().stream();

        if (number != null)
            conductorStream = conductorStream.filter(d -> d.getNumber().equalsIgnoreCase(number));
        if (crewStatusId != null)
            conductorStream = conductorStream.filter(d -> d.getCrewstatus().getId() == Integer.parseInt(crewStatusId));
        if (routeFamiliarityLevelId != null)
            conductorStream = conductorStream.filter(d -> d.getRoutefamiliaritylevel().getId() == Integer.parseInt(routeFamiliarityLevelId));

        return conductorMapper.toDtoList(conductorStream.collect(Collectors.toList()));
    }

    /**
     * Creates a new conductor.
     *
     * @param dto the {@link ConductorCreateRequestDto} containing the details of the conductor to be created.
     * @return the created {@link ConductorDetailResponseDto}.
     * @throws BusinessRuleViolationException if the crew status is not "ELIGIBLE" or the route familiarity level is not "LOW".
     */
    @Transactional
    public ConductorDetailResponseDto createConductor(@Valid @NotNull ConductorCreateRequestDto dto) {

        ConductorValidationContext context = conductorContextBuilder.buildForCreate(dto);

        validationStrategies.forEach(s -> s.validateCreate(context));

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new BusinessRuleViolationException("New conductor must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new BusinessRuleViolationException("New conductor route familiarity must have 'LOW'");
        }

        Conductor entity = conductorMapper.toEntity(dto);
        entity.setNumber(numberGeneratorService.nextConductorNumber());
        Conductor saved = conductorRepository.save(entity);

        return conductorMapper.toDto(saved);
    }

    /**
     * Updates an existing conductor.
     *
     * @param dto the {@link ConductorUpdateRequestDto} containing the updated details of the conductor.
     * @return the updated {@link ConductorDetailResponseDto}.
     * @throws ResourceNotFoundException if the conductor with the specified ID is not found.
     */
    @Transactional
    public ConductorDetailResponseDto updateConductor(@Valid @NotNull ConductorUpdateRequestDto dto) {

        Conductor existing = conductorRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor not found"));

        ConductorValidationContext context = conductorContextBuilder.buildForUpdate(dto);
        validationStrategies.forEach(s -> s.validateUpdate(context));

        Conductor entity = conductorMapper.updateEntityFromDto(dto, existing);

        if (dto.getRoutefamiliaritylevel().getId() != null) {
            RouteFamiliarityLevel targetRouteFamiliarityLevel = routeFamiliarityLevelService.getById(dto.getRoutefamiliaritylevel().getId());
            entity.setRoutefamiliaritylevel(targetRouteFamiliarityLevel);
        }

        if (dto.getCrewstatus().getId() != null) {
            CrewStatus targetStatus = crewStatusService.getById(dto.getCrewstatus().getId());
            entity.setCrewstatus(targetStatus);
        }

        return conductorMapper.toDto(entity);
    }

}
