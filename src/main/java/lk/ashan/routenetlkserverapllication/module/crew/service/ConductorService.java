package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.dto.*;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.ConductorMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;
    private final CrewEligibilityService crewEligibilityService;

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
        crewEligibilityService.validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());
        validateUniqueness(dto);

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new InvalidStatusException("New conductor must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new InvalidStatusException("New conductor route familiarity must have 'LOW'");
        }

        Conductor conductor = conductorMapper.toEntity(dto);
        return conductorMapper.toDto(conductorRepository.save(conductor));
    }

    public ConductorDetailResponseDto updateConductor(@Valid @NotNull ConductorUpdateRequestDto dto) {

        Conductor existingConductor =  conductorRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor not found"));

        crewEligibilityService.validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());
        crewEligibilityService.validateRouteFamiliarityTransition(existingConductor.getRoutefamiliaritylevel().getName(),dto.getRoutefamiliaritylevel().getName());

        validateUniqueness(dto);

        Conductor conductor = conductorMapper.toEntity(dto);
        return conductorMapper.toDto(conductorRepository.save(conductor));
    }

    private void validateUniqueness(ConductorCreateRequestDto dto) {
        if (conductorRepository.existsByNumber(dto.getNumber())) {
            throw new ValidationException("Conductor number already exists");
        }
    }

    private void validateUniqueness(ConductorUpdateRequestDto dto) {
        // Driver number uniqueness
        if (conductorRepository.existsByNumberAndIdNot(dto.getNumber(), dto.getId())) {
            throw new ResourceExistsException("Conductor number already exists");
        }
    }

}

