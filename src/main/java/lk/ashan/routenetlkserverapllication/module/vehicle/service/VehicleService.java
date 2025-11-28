package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
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
    public VehicleDetailResponseDto createVehicle(@Valid VehicleCreateRequestDto request){

        validateVehicleUniquenessForCreate(request);

        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
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

}
