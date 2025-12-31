package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.dto.*;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.ConductorMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleValidationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;

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
        validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());
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

    private void validateMedicalDates(LocalDate issued, LocalDate expiry) {
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleValidationException("Medical issued date cannot be in the future");
        }
        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleValidationException("Medical expiry must be after issued date");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);
        if (months > 6) {
            throw new BusinessRuleValidationException("Medical validity cannot exceed 6 months");
        }
    }

    private void validateUniqueness(ConductorCreateRequestDto dto) {
        if (conductorRepository.existsByNumber(dto.getNumber())) {
            throw new ValidationException("Conductor number already exists");
        }
    }

}

