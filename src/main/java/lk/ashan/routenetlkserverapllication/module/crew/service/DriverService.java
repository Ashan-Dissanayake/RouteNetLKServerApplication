package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.dto.*;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.DriverMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.model.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.crew.state.RouteFamiliarityState;
import lk.ashan.routenetlkserverapllication.module.crew.state.RouteFamiliarityStateFactory;
import lk.ashan.routenetlkserverapllication.module.crew.validation.DriverValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final List<DriverValidationStrategy> validationStrategies;
    private final RouteFamiliarityStateFactory routeFamiliarityStateFactory;

    public List<DriverDetailResponseDto> getDrivers(){
       return driverMapper.toDtoList(driverRepository.findAll());
    }

    public List<DriverDetailResponseDto> searchDriver(
            @NotNull HashMap<String, String> params) {

        String number = params.get("ssnumber");
        String crewStatusId = params.get("sscrewstatus");
        String routeFamiliarityLevelId = params.get("ssroutefamilitylevel");

        Stream<Driver> driverStream = driverRepository.findAll().stream();

        if (number != null)
            driverStream = driverStream.filter(d->d.getNumber().equalsIgnoreCase(number));
        if (crewStatusId != null)
            driverStream = driverStream.filter(d->d.getCrewstatus().getId()==Integer.parseInt(crewStatusId));
        if (routeFamiliarityLevelId != null)
            driverStream = driverStream.filter(d -> d.getRoutefamiliaritylevel().getId()== Integer.parseInt(routeFamiliarityLevelId));

        return driverMapper.toDtoList(driverStream.collect(Collectors.toList()));

    }

    public DriverDetailResponseDto createDriver(@Valid @NotNull DriverCreateRequestDto dto) {

        validationStrategies.forEach(s -> s.validateCreate(dto));

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new InvalidStatusException("New driver must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new InvalidStatusException("New driver route familiarity must have 'LOW'");
        }

        Driver driver = driverMapper.toEntity(dto);
        return driverMapper.toDto(driverRepository.save(driver));
    }

    public DriverDetailResponseDto updateDriver(@Valid @NotNull DriverUpdateRequestDto dto) {

        validationStrategies.forEach(s -> s.validateUpdate(dto));

        Driver existingDriver =  driverRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        Routefamiliaritylevel currentLevel = existingDriver.getRoutefamiliaritylevel();

        // State Pattern Transition
        if (!currentLevel.getName().equalsIgnoreCase(dto.getRoutefamiliaritylevel().getName())) {
             RouteFamiliarityState state = routeFamiliarityStateFactory.getState(currentLevel.getName());
             state.transitionTo(existingDriver.getEmployee(), driverMapper.toEntity(dto).getRoutefamiliaritylevel());
        }

        Driver driver = driverMapper.toEntity(dto);
        return driverMapper.toDto(driverRepository.save(driver));
    }

}
