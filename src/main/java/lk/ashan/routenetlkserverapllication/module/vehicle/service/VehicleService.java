package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
