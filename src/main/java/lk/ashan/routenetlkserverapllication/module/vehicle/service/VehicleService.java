package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateFactory;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableBranchFilter;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableUserFilter;
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
    private final VehicleStatusService vehicleStatusService;
    private final BusTypeService busTypeService;
    private final ConditionRateService conditionRateService;
    private final FuelTypeService fuelTypeService;
    private final BranchService branchService;
    private final ModelService modelService;
    private final VehicleMapper vehicleMapper;

    private final VehicleStateFactory vehicleStateFactory;
    private final VehicleStateTransitionHandler vehicleStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<VehicleDetailResponseDto> getVehicles(){
       return vehicleMapper.toDtoList(vehicleRepository.findAll());
    }

    @Transactional(readOnly = true)
    @DisableUserFilter
    public List<VehicleSummaryDto> getVehicleSummary(){
        return vehicleMapper.toSummaryDtoList(vehicleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<VehicleDetailResponseDto> searchVehicle(@NotNull HashMap<String, String> params) {

        String conditionrateid = params.get("ssconditionrate");
        String bustypeId = params.get("ssbustype");
        String mileageRangeId = params.get("ssmileagerange");

        Stream<Vehicle> vehicleStream = vehicleRepository.findAll().stream();

        if (bustypeId != null) vehicleStream = vehicleStream.filter(v->v.getBustype().getId()==Integer.parseInt(bustypeId));
        if (conditionrateid != null) vehicleStream = vehicleStream.filter(v -> v.getConditionrate().getId() == Integer.parseInt(conditionrateid));

        return vehicleMapper.toDtoList(vehicleStream.collect(Collectors.toList()));

    }

    @Transactional(readOnly = true)
    public Vehicle getById(Integer id){
        return vehicleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle not found"));
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public VehicleDetailResponseDto createVehicle(@Valid @NotNull VehicleCreateRequestDto request){

        if (vehicleRepository.existsByNumber(request.getNumber())) {
            throw new ResourceExistsException("Vehicle number already exists.");
        }

        Vehicle entity = vehicleMapper.toEntity(request);

        VehicleStatus initialStatus = vehicleStatusService.getByName(request.getVehiclestatus().getName());
        vehicleStateFactory.getState(initialStatus.getName())
                .validateInitial();
        entity.setVehiclestatus(initialStatus);

        Vehicle savedVehicle = vehicleRepository.save(entity);

        return vehicleMapper.toDto(savedVehicle);
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public VehicleDetailResponseDto updateVehicle(@Valid @NotNull VehicleUpdateRequestDto request) {

        Vehicle existingVehicle = vehicleRepository.findByMyId(request.getId());

        if (request.getMileage() != null && existingVehicle.getMileage() != null) {
            if (request.getMileage() < existingVehicle.getMileage()) {
                throw new BusinessRuleViolationException("Mileage cannot be less than current value.");
            }
        }

        vehicleMapper.updateEntityFromDto(request,existingVehicle);

        ConditionRate currentConditionRate = existingVehicle.getConditionrate();

        validateConditionRateTransition(currentConditionRate.getName(), request.getConditionrate().getName());

        if (request.getVehiclestatus().getId() != null) {
            VehicleStatus targetStatus = vehicleStatusService.getById(request.getVehiclestatus().getId());
            vehicleStateTransitionHandler.transitionTo(existingVehicle, targetStatus);
        }
//
//        if (request.getBranch().getId()!=null){
//            Branch targetBranch = branchService.getById(request.getBranch().getId());
//            existingVehicle.setBranch(targetBranch);
//        }

        if (request.getBustype().getId()!=null){
            BusType targetBuType = busTypeService.getById(request.getBustype().getId());
            existingVehicle.setBustype(targetBuType);
        }

        if (request.getConditionrate().getId()!=null){
            ConditionRate targetConditionRate = conditionRateService.getById(request.getConditionrate().getId());
            existingVehicle.setConditionrate(targetConditionRate);
        }

        if (request.getFueltype().getId()!=null){
            FuelType targetFuelType = fuelTypeService.getById(request.getFueltype().getId());
            existingVehicle.setFueltype(targetFuelType);
        }

        if (request.getModel().getId()!=null){
            Model targetModel = modelService.getById(request.getModel().getId());
            existingVehicle.setModel(targetModel);
        }

        return vehicleMapper.toDto(existingVehicle);
    }

    @Transactional
    public List<Integer> deactivateVehicle(List<Integer> vehicleIds) {
        List<Vehicle> vehicles = vehicleRepository.findAllById(vehicleIds);

        if (vehicles.isEmpty())
            throw new ResourceNotFoundException("No vehicles found for the given IDs");

        vehicleRepository.removeAll(vehicleIds);

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
