package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.ConditionRate;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleState;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateFactory;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehiclestatusService vehiclestatusService;
    private final VehicleMapper vehicleMapper;
    private final VehicleStateFactory vehicleStateFactory;

    @Transactional(readOnly = true)
    public List<VehicleDetailResponseDto> getVehicles(){
       return vehicleMapper.toDtoList(vehicleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<VehicleDetailResponseDto> searchVehicle(@NotNull HashMap<String, String> params) {

        String conditionrateid = params.get("ssconditionrate");
        String bustypeId = params.get("ssbustype");

        Stream<Vehicle> vehicleStream = vehicleRepository.findAll().stream();

        if (bustypeId != null) vehicleStream = vehicleStream.filter(v->v.getBustype().getId()==Integer.parseInt(bustypeId));
        if (conditionrateid != null)
            vehicleStream = vehicleStream.filter(v -> v.getConditionrate().getId() == Integer.parseInt(conditionrateid));

        return vehicleMapper.toDtoList(vehicleStream.collect(Collectors.toList()));

    }

    @Transactional
    @DisableSoftDeleteFilter
    public VehicleDetailResponseDto createVehicle(@Valid @NotNull VehicleCreateRequestDto request){

        if (vehicleRepository.existsByNumber(request.getNumber())) {
            throw new ResourceExistsException("Vehicle number already exists.");
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);

        VehicleStatus initialStatus = vehiclestatusService.getByName(request.getVehiclestatus().getName());
        vehicleStateFactory.getState(initialStatus.getName())
                .validateInitial();
        vehicle.setVehiclestatus(initialStatus);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public VehicleDetailResponseDto updateVehicle(@Valid @NotNull VehicleUpdateRequestDto request) {

        Vehicle existingVehicle = vehicleRepository.findByMyId(request.getId());

        if (request.getMileage() < existingVehicle.getMileage()) {
            throw new BusinessRuleViolationException("Mileage cannot be less than current value.");
        }

        ConditionRate currentConditionRate = existingVehicle.getConditionrate();
        VehicleStatus currentStatus = existingVehicle.getVehiclestatus();

        validateConditionRateTransition(currentConditionRate.getName(), request.getConditionrate().getName());

        if (!currentStatus.getName().equalsIgnoreCase(request.getVehiclestatus().getName())) {
            VehicleState state = vehicleStateFactory.getState(currentStatus.getName());
            state.transitionTo(existingVehicle, currentStatus);
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(updatedVehicle);
    }

    @Transactional
    public List<Integer> deactivateVehicle(List<Integer> vehicleIds) {
        List<Vehicle> vehicles = vehicleRepository.findAllById(vehicleIds);

        if (vehicles.isEmpty())
            throw new ResourceNotFoundException("No vehicles found for the given IDs");

        vehicleRepository.removeAll(vehicleIds);

        return vehicles.stream() .map(Vehicle::getId) .collect(Collectors.toList());
    }

    @Transactional
    public List<Integer> activateVehicle(List<Integer> vehicleIds) {
        List<Vehicle> vehicles = vehicleRepository.findAllById(vehicleIds);

        if (vehicles.isEmpty())
            throw new ResourceNotFoundException("No vehicles found for the given IDs");

        vehicleRepository.restoreAll(vehicleIds);

        return vehicles.stream() .map(Vehicle::getId) .collect(Collectors.toList());
    }


    private void validateConditionRateTransition(String currentRate, String newRate) {

        if (currentRate == null || newRate == null) {
            throw new IllegalArgumentException("Rate cannot be null.");
        }

        if (currentRate.equalsIgnoreCase(newRate)) return;

        currentRate = currentRate.trim().toUpperCase();
        newRate = newRate.trim().toUpperCase();

        List<String> allowedRates = VALID_CONDITION_TRANSITIONS.get(currentRate);

        if (allowedRates == null) {
            throw new IllegalArgumentException("Unknown current Rate: " + currentRate);
        }

        if (!allowedRates.contains(newRate)) {
            throw new InvalidStateTransitionException(
                    "Invalid Rate transition from " + currentRate + " to " + newRate
            );
        }
    }

    private static final Map<String, List<String>> VALID_CONDITION_TRANSITIONS = Map.of(
            "EXCELLENT", List.of("GOOD"),
            "GOOD",      List.of("FAIR"),
            "FAIR",      List.of("POOR"),
            "POOR",      List.of("CRITICAL"),
            "CRITICAL",  List.of() // terminal state
    );

}
