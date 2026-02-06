package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Conditionrate;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleState;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateFactory;
import lk.ashan.routenetlkserverapllication.module.vehicle.validation.VehicleValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;
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
    private final VehicleMapper vehicleMapper;
    private final List<VehicleValidationStrategy> validationStrategies;
    private final VehicleStateFactory vehicleStateFactory;

    public List<VehicleDetailResponseDto> getVehicles(){
       return vehicleMapper.toDtoList(vehicleRepository.findAll());
    }

    public List<VehicleDetailResponseDto> searchVehicle(@NotNull HashMap<String, String> params) {

        String servicetypeid = params.get("sservicetype");
        String conditionrateid = params.get("ssconditionrate");

        Stream<Vehicle> vehicleStream = vehicleRepository.findAll().stream();

        if (servicetypeid != null) vehicleStream = vehicleStream.filter(v->v.getVehiclestatus().getId()==Integer.parseInt(servicetypeid));
        if (conditionrateid != null)
            vehicleStream = vehicleStream.filter(v -> v.getConditionrate().getId() == Integer.parseInt(conditionrateid));

        return vehicleMapper.toDtoList(vehicleStream.collect(Collectors.toList()));

    }

    @Transactional
    @DisableSoftDeleteFilter
    public VehicleDetailResponseDto createVehicle(@Valid @NotNull VehicleCreateRequestDto request){

        // Execute all validation strategies
        validationStrategies.forEach(strategy -> strategy.validateCreate(request));

        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public VehicleDetailResponseDto updateVehicle(@Valid @NotNull VehicleUpdateRequestDto request) {

        validationStrategies.forEach(strategy -> strategy.validateUpdate(request));

        Vehicle existingVehicle = vehicleRepository.findByMyId(request.getId());
        Conditionrate currentConditionrate = existingVehicle.getConditionrate();
        Vehiclestatus currentStatus = existingVehicle.getVehiclestatus();

        validateConditionRateTransition(currentConditionrate.getName(), request.getConditionrate().getName());

        // State Pattern for Status Transition
        if (!currentStatus.getName().equalsIgnoreCase(request.getVehiclestatus().getName())) {
            VehicleState state = vehicleStateFactory.getState(currentStatus.getName());
            // The State implementation validates the transition
            // Note: In a full State pattern, the State object might also apply the change,
            // but here we are using it for validation logic primarily as per requirement to replace the map.
             // We pass the entity so the State *could* modify it if needed, or just validate.
            state.transitionTo(existingVehicle, vehicleMapper.toEntity(request).getVehiclestatus());
        }

        // Mapping updates to entity
        Vehicle vehicle = vehicleMapper.toEntity(request);
        // Ensure ID is set for update
        vehicle.setId(request.getId());
        
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
            throw new InvalidStatusTransitionException(
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
