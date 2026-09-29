package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.validation.VehicleValidator;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableBranchFilter;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableUserFilter;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleStatusService vehicleStatusService;
    private final BusTypeService busTypeService;
    private final ConditionRateService conditionRateService;
    private final FuelTypeService fuelTypeService;
    private final ModelService modelService;
    private final VehicleMapper vehicleMapper;
    private final VehicleValidator vehicleValidator;

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

        Specification<Vehicle> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String conditionrateid = params.get("ssconditionrate");
            String bustypeId = params.get("ssbustype");

            if (bustypeId != null && !bustypeId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("bustype").get("id"),
                                Integer.parseInt(bustypeId)
                        )
                );
            }

            if (conditionrateid != null && !conditionrateid.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("conditionrate").get("id"),
                                Integer.parseInt(conditionrateid)
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<Vehicle> vehicles = vehicleRepository.findAll(specification);

        return vehicleMapper.toDtoList(vehicles);
    }

    @Transactional(readOnly = true)
    public Vehicle getById(Integer id){
        return vehicleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle not found"));
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public VehicleDetailResponseDto createVehicle(@Valid @NotNull VehicleCreateRequestDto request) {

        vehicleValidator.validateCreate(request);

        Vehicle entity = vehicleMapper.toEntity(request);

        VehicleStatus initialStatus = vehicleStatusService.getByName(request.getVehiclestatus().getName());
        vehicleValidator.validateInitialStatus(initialStatus);
        entity.setVehiclestatus(initialStatus);

        Vehicle savedVehicle = vehicleRepository.save(entity);

        return vehicleMapper.toDto(savedVehicle);
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public VehicleDetailResponseDto updateVehicle(@Valid @NotNull VehicleUpdateRequestDto request) {

        Vehicle existingVehicle = vehicleRepository.findByMyId(request.getId());

        vehicleValidator.validateUpdate(existingVehicle, request);

        vehicleMapper.updateEntityFromDto(request, existingVehicle);

        if (request.getVehiclestatus() != null && request.getVehiclestatus().getId() != null) {
            VehicleStatus targetStatus = vehicleStatusService.getById(request.getVehiclestatus().getId());
            changeStatus(existingVehicle, targetStatus);
        }

        if (request.getBustype().getId() != null) {
            BusType targetBusType = busTypeService.getById(request.getBustype().getId());
            existingVehicle.setBustype(targetBusType);
        }

        if (request.getConditionrate().getId() != null) {
            ConditionRate targetConditionRate = conditionRateService.getById(request.getConditionrate().getId());
            existingVehicle.setConditionrate(targetConditionRate);
        }

        if (request.getFueltype().getId() != null) {
            FuelType targetFuelType = fuelTypeService.getById(request.getFueltype().getId());
            existingVehicle.setFueltype(targetFuelType);
        }

        if (request.getModel().getId() != null) {
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

    private void changeStatus(Vehicle vehicle, VehicleStatus targetStatus) {
        vehicleValidator.validateStatusTransition(
                vehicle.getVehiclestatus(),
                targetStatus
        );

        vehicle.setVehiclestatus(targetStatus);
    }

}
