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
import lk.ashan.routenetlkserverapllication.module.crew.state.RouteFamiliarityState;
import lk.ashan.routenetlkserverapllication.module.crew.state.RouteFamiliarityStateFactory;
import lk.ashan.routenetlkserverapllication.module.crew.validation.ConductorValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<ConductorDetailResponseDto> getConductors(){
       return conductorMapper.toDtoList(conductorRepository.findAll());
    }

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

    public ConductorDetailResponseDto createConductor(@Valid @NotNull ConductorCreateRequestDto dto) {
        
        validationStrategies.forEach(s -> s.validateCreate(dto));

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new ValidationException("New conductor must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new ValidationException("New conductor route familiarity must have 'LOW'");
        }

        Conductor conductor = conductorMapper.toEntity(dto);
        return conductorMapper.toDto(conductorRepository.save(conductor));
    }

    public ConductorDetailResponseDto updateConductor(@Valid @NotNull ConductorUpdateRequestDto dto) {

        validationStrategies.forEach(s -> s.validateUpdate(dto));

        Conductor existingConductor =  conductorRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor not found"));

        RouteFamiliarityLevel currentLevel = existingConductor.getRoutefamiliaritylevel();

        // State Pattern Transition
        if (!currentLevel.getName().equalsIgnoreCase(dto.getRoutefamiliaritylevel().getName())) {
             RouteFamiliarityState state = routeFamiliarityStateFactory.getState(currentLevel.getName());
             state.transitionTo(existingConductor.getEmployee(), conductorMapper.toEntity(dto).getRoutefamiliaritylevel());
        }

        Conductor conductor = conductorMapper.toEntity(dto);
        return conductorMapper.toDto(conductorRepository.save(conductor));
    }

}

