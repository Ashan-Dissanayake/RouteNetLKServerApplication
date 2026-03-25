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


    @Transactional(readOnly = true)
    public List<ConductorDetailResponseDto> getConductors(){
       return conductorMapper.toDtoList(conductorRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ConductorDetailResponseDto> searchConductor(@NotNull HashMap<String, String> params) {

        String number = params.get("ssnumber");
        String crewStatusId = params.get("sscrewstatus");
        String routeFamiliarityLevelId = params.get("ssroutefamilitylevel");

        Stream<Conductor> conductorStream = conductorRepository.findAll().stream();

        if (number != null)
            conductorStream = conductorStream.filter(d->d.getNumber().equalsIgnoreCase(number));
        if (crewStatusId != null)
            conductorStream = conductorStream.filter(d->d.getCrewstatus().getId()==Integer.parseInt(crewStatusId));
        if (routeFamiliarityLevelId != null)
            conductorStream = conductorStream.filter(d -> d.getRoutefamiliaritylevel().getId()== Integer.parseInt(routeFamiliarityLevelId));

        return conductorMapper.toDtoList(conductorStream.collect(Collectors.toList()));

    }

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

    @Transactional
    public ConductorDetailResponseDto updateConductor(@Valid @NotNull ConductorUpdateRequestDto dto) {

        Conductor existing = conductorRepository.findById(dto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Conductor not found"));

        ConductorValidationContext context = conductorContextBuilder.buildForUpdate(dto);
        validationStrategies.forEach(s -> s.validateUpdate(context));

        Conductor entity =  conductorMapper.updateEntityFromDto(dto, existing);

      if (dto.getRoutefamiliaritylevel().getId()!=null){
            RouteFamiliarityLevel targetRouteFamiliarityLevel = routeFamiliarityLevelService.getById(dto.getRoutefamiliaritylevel().getId());
            entity.setRoutefamiliaritylevel(targetRouteFamiliarityLevel);
        }

        if (dto.getCrewstatus().getId()!=null){
            CrewStatus targetStatus = crewStatusService.getById(dto.getCrewstatus().getId());
            entity.setCrewstatus(targetStatus);
        }

        return conductorMapper.toDto(entity);
    }

}



