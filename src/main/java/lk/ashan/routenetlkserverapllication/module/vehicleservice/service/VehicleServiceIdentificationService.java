package lk.ashan.routenetlkserverapllication.module.vehicleservice.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeService;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.service.PartService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper.VehicleServiceMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceExecution;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePart;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServiceExecutionRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.validation.VehicleServiceValidator;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceIdentificationService {

    private final VehicleServiceRepository vehicleServiceRepository;
    private final VehicleServiceMapper vehicleServiceMapper;
    private final NumberGeneratorService numberGeneratorService;
    private final VehicleServiceStatusService vehicleServiceStatusService;
    private final BranchService branchService;
    private final PartService partService;
    private final EmployeeService employeeService;
    private final VehicleServiceExecutionRepository vehicleServiceExecutionRepository;
    private final VehicleServiceValidator vehicleServiceValidator;

    @Transactional(readOnly = true)
    public List<VehicleServiceDetailResponseDto> getVehicleServices() {
        return vehicleServiceMapper.toDtoList(
                vehicleServiceRepository.findAll()
        );
    }

    @Transactional(readOnly = true)
    public List<VehicleServiceDetailResponseDto> searchVehicleService(@NotNull HashMap<String, String> params) {

        Specification<VehicleService> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    String vehicleId = params.get("ssvehicle");
                    String doCreated = params.get("ssdocreated");

                    if (vehicleId != null && !vehicleId.isBlank()) {

                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("vehicle").get("id"),
                                        Integer.parseInt(vehicleId)
                                )
                        );
                    }

                    if (doCreated != null && !doCreated.isBlank()) {
                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("docreated"),
                                        LocalDate.parse(doCreated)
                                )
                        );
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };

        List<VehicleService> vehicleServices = vehicleServiceRepository.findAll(specification);

        return vehicleServiceMapper.toDtoList(vehicleServices);
    }

    @Transactional
    public VehicleServiceDetailResponseDto createVehicleService(@Valid @NotNull VehicleServiceCreateRequestDto request) {

        Branch branch = branchService.getById(request.getBranch().getId());

        VehicleServiceStatus initialStatus = vehicleServiceStatusService.getByName(
                        request.getVehicleservicestatus()
                                .getName()
                );

        vehicleServiceValidator.validateCreate(request);

        VehicleService service = vehicleServiceMapper.toEntity(request);
        service.setVehicleservicestatus(initialStatus);
        service.setDocreated(LocalDate.now());
        service.setNumber(numberGeneratorService.nextVehicleServiceNumber(
                        branch.getCode(),
                        YearMonth.now()
                )
        );

        if (request.getVehicleserviceparts() != null && !request.getVehicleserviceparts().isEmpty()) {

            for (VehicleServicePartDto partDto : request.getVehicleserviceparts()) {

                Part part = partService.getById(partDto.getPart().getId());

                VehicleServicePart servicePart = new VehicleServicePart();
                servicePart.setPart(part);
                servicePart.setQuantity(partDto.getQuantity());
                service.addPart(servicePart);
            }
        }

        VehicleService savedService = vehicleServiceRepository.save(service);

        return vehicleServiceMapper.toDto(savedService);
    }

    @Transactional
    public VehicleServiceDetailResponseDto startExecution(Integer id, VehicleServiceStartRequestDto dto) {

        VehicleService service = getById(id);

        VehicleServiceStatus targetStatus = vehicleServiceStatusService.getByName("In Progress");
        changeStatus(service, targetStatus);

        VehicleServiceExecution execution = new VehicleServiceExecution();

        execution.setVehicleservice(service);
        execution.setBranch(service.getBranch());
        execution.setDostarted(LocalDate.now());
        execution.setStartodometer(dto.getStartodometer());
        execution.setMaintechnician(employeeService.getById(dto.getMaintechnicianId()));

        vehicleServiceExecutionRepository.save(execution);
        vehicleServiceRepository.save(service);

        return vehicleServiceMapper.toDto(service);
    }

    @Transactional
    public VehicleServiceDetailResponseDto placeOnHold(Integer id) {

        VehicleService service = getById(id);

        VehicleServiceStatus targetStatus = vehicleServiceStatusService.getByName("On Hold Parts");
        changeStatus(service, targetStatus);

        vehicleServiceRepository.save(service);
        return vehicleServiceMapper.toDto(service);
    }

    @Transactional
    public VehicleServiceDetailResponseDto complete(Integer id, VehicleServiceCompleteRequestDto dto) {

        VehicleService service = getById(id);

        VehicleServiceExecution activeExecution =
                vehicleServiceExecutionRepository
                        .findByVehicleserviceAndDoendIsNull(
                                service
                        )
                        .orElseThrow(() ->
                                new BusinessRuleViolationException(
                                        "No active execution segment found to complete"
                                )
                        );

        VehicleServiceStatus targetStatus = vehicleServiceStatusService.getByName("Completed");

        changeStatus(service, targetStatus);

        vehicleServiceMapper.updateExecutionWithCompletePayload(dto, activeExecution);

        activeExecution.setDoend(LocalDate.now());
        activeExecution.setNextserviceinkm(activeExecution.getStartodometer() + dto.getServiceIntervalKm());

        vehicleServiceExecutionRepository.save(activeExecution);
        vehicleServiceRepository.save(service);

        return vehicleServiceMapper.toDto(service);
    }

    private void changeStatus(VehicleService service, VehicleServiceStatus targetStatus) {
        vehicleServiceValidator.validateStatusTransition(service.getVehicleservicestatus(), targetStatus);
        service.setVehicleservicestatus(targetStatus);
    }

    private VehicleService getById(Integer id) {

        return vehicleServiceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle service ticket not found with id: "
                                        + id
                        )
                );
    }
}