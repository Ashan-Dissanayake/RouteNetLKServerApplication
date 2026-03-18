package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.ConductorMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.state.routefamility.RouteFamiliarityState;
import lk.ashan.routenetlkserverapllication.module.crew.state.routefamility.RouteFamiliarityStateFactory;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.ConductorContextBuilder;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.ConductorValidationContext;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.ConductorValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
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

    private final List<ConductorValidationStrategy> validationStrategies;
    private final RouteFamiliarityStateFactory routeFamiliarityStateFactory;
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
            throw new ValidationException("New conductor must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new ValidationException("New conductor route familiarity must have 'LOW'");
        }

        Conductor conductor = conductorMapper.toEntity(dto);
        return conductorMapper.toDto(conductorRepository.save(conductor));
    }

    @Transactional
    public ConductorDetailResponseDto updateConductor(@Valid @NotNull ConductorUpdateRequestDto dto) {

        ConductorValidationContext context = conductorContextBuilder.buildForUpdate(dto);

        validationStrategies.forEach(s -> s.validateUpdate(context));

        Conductor existing =  conductorRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor not found"));

        RouteFamiliarityLevel currentLevel = existing.getRoutefamiliaritylevel();

        // State Pattern Transition
        if (!currentLevel.getName().equalsIgnoreCase(dto.getRoutefamiliaritylevel().getName())) {
             RouteFamiliarityState state = routeFamiliarityStateFactory.getState(currentLevel.getName());
             state.transitionTo(existing.getEmployee(), conductorMapper.toEntity(dto).getRoutefamiliaritylevel());
        }

      Conductor mappedConductor =  conductorMapper.updateEntityFromDto(dto,existing);
        return conductorMapper.toDto(mappedConductor);
    }

}

