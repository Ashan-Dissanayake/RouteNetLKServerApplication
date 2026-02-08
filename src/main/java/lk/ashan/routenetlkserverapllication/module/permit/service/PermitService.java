package lk.ashan.routenetlkserverapllication.module.permit.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.*;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitStatusRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitState;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitStatusFactory;
import lk.ashan.routenetlkserverapllication.module.permit.validation.PermitValidationContext;
import lk.ashan.routenetlkserverapllication.module.permit.validation.PermitValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PermitService {

    private final PermitRepository permitRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final PermitStatusRepository permitStatusRepository;
    private final PermitMapper permitMapper;

    private final List<PermitValidationStrategy> validationStrategies;
    private final PermitStatusFactory permitStatusFactory;


    public List<PermitDetailResponseDto> getPermits(){
        return permitMapper.toDtoList(permitRepository.findAll());
    }

    public List<PermitDetailResponseDto> searchPermit(@NotNull HashMap<String, String> params) {

        String number = params.get("ssnumber");
        String permitStatusId = params.get("sspermitstatus");
        String routeId = params.get("ssroute");

        Stream<Permite> permitStream = permitRepository.findAll().stream();

        if (number != null) permitStream = permitStream.filter(v->v.getNumber().equalsIgnoreCase(number));
        if (permitStatusId != null) permitStream = permitStream.filter(v->v.getPermitestatus().getId()==Integer.parseInt(permitStatusId));
        if (routeId != null)
            permitStream = permitStream.filter(v -> v.getRoute().getId() == Integer.parseInt(routeId));

        return permitMapper.toDtoList(permitStream.collect(Collectors.toList()));

    }

    @Transactional
    @DisableSoftDeleteFilter
    public PermitDetailResponseDto createPermit(@NotNull PermitCreateRequestDto requestDto) {

        if (permitRepository.existsByNumber(requestDto.getNumber())) {
            throw new ResourceExistsException("Permit number already exists.");
        }

        Vehicle vehicle = vehicleRepository.findByNumber(requestDto.getVehicle().getNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Route route = routeRepository.findByNumber(requestDto.getRoute().getNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        Servicetype serviceType = serviceTypeRepository
                .findByName(requestDto.getServicetype().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Service type not found"));

        PermitValidationContext context = PermitValidationContext.builder()
                .permitNumber(requestDto.getNumber())
                .vehicleId(vehicle.getId())
                .routeId(route.getId())
                .vehicleBranchId(vehicle.getBranch().getId())
                .requestBranchId(requestDto.getBranch().getId())
                .busType(vehicle.getBustype())
                .routeType(route.getRoutetype())
                .serviceType(serviceType)
                .doissued(requestDto.getDoissued())
                .doexpired(requestDto.getDoexpired())
                .build();

        validationStrategies.forEach(strategy -> strategy.validate(context));

        Permite permite = permitMapper.toEntity(requestDto);

        Permitestatus requestedStatus = permitStatusRepository
                .findByName(requestDto.getPermitestatus().getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permit status not found: " + requestDto.getPermitestatus().getName()));

        PermitState state = permitStatusFactory.getState(requestedStatus.getName());
        state.validateInitial();

        permite.setPermitestatus(requestedStatus);

        Permite saved = permitRepository.save(permite);
        return permitMapper.toDto(saved);
    }


}
