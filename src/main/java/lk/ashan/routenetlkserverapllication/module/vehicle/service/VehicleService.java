package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Conditionrate;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Seatingcapacity;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.SeatingcapacityRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
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
    private final SeatingcapacityRepository seatingcapacityRepository;
    private final VehicleMapper vehicleMapper;

    public List<VehicleDetailResponseDto> getVehicles(){
       return vehicleMapper.toDtoList(vehicleRepository.findAll());
    }

    public List<VehicleDetailResponseDto> searchVehicle(@NotNull HashMap<String, String> params) {

        String code = params.get("sscode");
        String servicetypeid = params.get("sservicetype");
        String conditionrateid = params.get("ssconditionrate");

        Stream<Vehicle> vehicleStream = vehicleRepository.findAll().stream();

        if (code != null)
            vehicleStream = vehicleStream.filter(v -> v.getCode().equalsIgnoreCase(code));
        if (servicetypeid != null) vehicleStream = vehicleStream.filter(v->v.getVehiclestatus().getId()==Integer.parseInt(servicetypeid));
        if (conditionrateid != null)
            vehicleStream = vehicleStream.filter(v -> v.getConditionrate().getId() == Integer.parseInt(conditionrateid));

        return vehicleMapper.toDtoList(vehicleStream.collect(Collectors.toList()));

    }

    @Transactional
    @DisableSoftDeleteFilter
    public VehicleDetailResponseDto createVehicle(@Valid @NotNull VehicleCreateRequestDto request){

        validateVehicleUniquenessForCreate(request);
        validateSeatingCapacityWithModel(request);

        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public VehicleDetailResponseDto updateVehicle(@Valid @NotNull VehicleUpdateRequestDto request) {

        Conditionrate currentConditionrate = vehicleRepository.findByMyId(request.getId()).getConditionrate();
        Vehiclestatus currentStatus = vehicleRepository.findByMyId(request.getId()).getVehiclestatus();

        validateConditionRateTransition(currentConditionrate.getName(), request.getConditionrate().getName());
        validateStatusTransition(currentStatus.getName(), request.getVehiclestatus().getName());
        validateMileageIncrement(request);

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
            throw new InvalidStatusTransitionException(
                    "Invalid Rate transition from " + currentRate + " to " + newRate
            );
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {

        if (currentStatus == null || newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        if (currentStatus.equalsIgnoreCase(newStatus)) return;

        currentStatus = currentStatus.trim().toUpperCase();
        newStatus = newStatus.trim().toUpperCase();

        List<String> allowedStatuses = VALID_STATUS_TRANSITIONS.get(currentStatus);

        if (allowedStatuses == null) {
            throw new IllegalArgumentException("Unknown current status: " + currentStatus);
        }

        if (!allowedStatuses.contains(newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }

    private void validateVehicleUniquenessForCreate(VehicleCreateRequestDto vehicle){
        if (vehicleRepository.existsByCode(vehicle.getCode())) {
            throw new ResourceExistsException("Vehicle code already exists.");
        }

        if (vehicleRepository.existsByNumber(vehicle.getNumber())) {
            throw new ResourceExistsException("Vehicle number already exists.");
        }

        if (vehicleRepository.existsByChasisnumber(vehicle.getChasisnumber())) {
            throw new ResourceExistsException("Vehicle chassis number already exists.");
        }

        if (vehicleRepository.existsByEnginenumber(vehicle.getEnginenumber())) {
            throw new ResourceExistsException("Vehicle engine number already exists.");
        }

        if (vehicleRepository.existsByCodeOrChasisnumber(vehicle.getCode(),vehicle.getChasisnumber())){
            throw new ResourceExistsException("Code cannot reference a chassis number already used by another vehicle");
        }

        if (vehicleRepository.existsByCodeOrEnginenumber(vehicle.getCode(),vehicle.getEnginenumber())){
            throw new ResourceExistsException("Code cannot reference a engine number already used by another vehicle");
        }

    }

    private void validateSeatingCapacityWithModel(VehicleCreateRequestDto vehicle) {

        Integer makeId = vehicle.getMake().getId();
        Integer amount = vehicle.getSeatingcapacity().getAmount();

        // 1. Get all allowed capacities for this model
        List<Seatingcapacity> allowedCapacities = seatingcapacityRepository.findByMakeId(makeId);

        if (allowedCapacities.isEmpty()) {
            throw new ResourceNotFoundException("No seating capacities found for the selected model.");
        }

        // 2. Check if requested capacity is one of them
        boolean isValid = allowedCapacities.stream()
                .anyMatch(s -> s.getAmount().equals(amount));

        if (!isValid) {
            throw new InvalidSeatingCapacityException(
                    "Selected seating capacity is not valid for the chosen model."
            );
        }
    }

    private void validateMileageIncrement(VehicleUpdateRequestDto vehicle){
        Integer currentMileage = vehicleRepository.findByMyId(vehicle.getId()).getMileage();
        if (vehicle.getMileage() <currentMileage) throw new InvalidMileageException("Mileage can not be Minus value");
    }

    private static final Map<String, List<String>> VALID_CONDITION_TRANSITIONS = Map.of(
            "EXCELLENT", List.of("GOOD"),
            "GOOD",      List.of("FAIR"),
            "FAIR",      List.of("POOR"),
            "POOR",      List.of("CRITICAL"),
            "CRITICAL",  List.of() // terminal state
    );

    private static final Map<String, List<String>> VALID_STATUS_TRANSITIONS = Map.of(
            "AVAILABLE", List.of("IN SERVICE", "RESERVED", "UNDER MAINTENANCE"),
            "IN SERVICE", List.of("AVAILABLE", "UNDER MAINTENANCE", "OUT OF SERVICE"),
            "UNDER MAINTENANCE", List.of("AVAILABLE", "OUT OF SERVICE", "DECOMMISSIONED"),
            "OUT OF SERVICE", List.of("UNDER MAINTENANCE", "DECOMMISSIONED"),
            "RESERVED", List.of("IN SERVICE", "AVAILABLE"),
            "DECOMMISSIONED", List.of() // terminal state
    );


}
