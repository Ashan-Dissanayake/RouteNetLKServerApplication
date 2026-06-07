package lk.ashan.routenetlkserverapllication.module.vehicleservice.service;

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
import lk.ashan.routenetlkserverapllication.module.vehicleservice.state.VehicleServiceStateFactory;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.state.VehicleServiceStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.validation.VehicleServiceCreationValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.validation.VehicleServiceValidationContext;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final VehicleServiceCreationValidationStrategy creationValidationStrategy;

    private final VehicleServiceStateFactory vehicleServiceStateFactory;
    private final VehicleServiceStateTransitionHandler vehicleServiceStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<VehicleServiceDetailResponseDto> getVehicleServices(){
        return vehicleServiceMapper.toDtoList(vehicleServiceRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<VehicleServiceDetailResponseDto> searchVehicleService(
            @NotNull HashMap<String, String> params) {

        String vehicleId = params.get("ssvehicle");
        String doCreated = params.get("ssdocreated");

        Stream<VehicleService> vehicleServiceStream = vehicleServiceRepository.findAll().stream();

        if (vehicleId != null) vehicleServiceStream = vehicleServiceStream.filter(v->v.getVehicle().getId()==Integer.parseInt(vehicleId));
        if (doCreated != null) vehicleServiceStream = vehicleServiceStream.filter(v -> v.getDocreated().isEqual(LocalDate.parse(doCreated)));

        return vehicleServiceMapper.toDtoList(vehicleServiceStream.collect(Collectors.toList()));
    }

    @Transactional
    public VehicleServiceDetailResponseDto createVehicleService(@Valid @NotNull VehicleServiceCreateRequestDto request) {

        // 1. System Existence Check (Structural Gatekeeper)
        Branch branch = branchService.getById(request.getBranch().getId());

        // 2. Fetch Status and Perform State Machine Entry Validation (Your Initial Gate passion)
        VehicleServiceStatus initialStatus = vehicleServiceStatusService.getByName(request.getVehicleservicestatus().getName());
        vehicleServiceStateFactory.getState(initialStatus.getName())
                .validateInitial(); // Will pass cleanly if status name is 'PENDING'

        // 3. Build Validation Context and Execute Business Rule Strategies
        VehicleServiceValidationContext validationContext = VehicleServiceValidationContext.builder()
                .branchId(request.getBranch().getId())
                .vehicleId(request.getVehicle().getId())
                .serviceTypeName(request.getVehicleservicetype().getName())
                .incidentId(request.getIncident() != null ? request.getIncident().getId() : null)
                .parts(request.getVehicleserviceparts())
                .build();
        creationValidationStrategy.validate(validationContext);

        // 4. Map DTO payload to core Entity
        VehicleService service = vehicleServiceMapper.toEntity(request);

        // 5. Apply Automated System-Controlled Audit Properties
        service.setVehicleservicestatus(initialStatus);
        service.setDocreated(LocalDate.now());
        service.setNumber(numberGeneratorService.nextVehicleServiceNumber(branch.getCode(), YearMonth.now()));

        // 6. Handle Child Master-Detail Mappings Defensively (Fixes NullPointerException & sets backward link)
        if (request.getVehicleserviceparts() != null && !request.getVehicleserviceparts().isEmpty()) {
            for (VehicleServicePartDto partDto : request.getVehicleserviceparts()) {
                Part part = partService.getById(partDto.getPart().getId());

                VehicleServicePart servicePart = new VehicleServicePart();
                servicePart.setPart(part);
                servicePart.setQuantity(partDto.getQuantity());

                service.addPart(servicePart);
            }
        }

        // 7. Persist complete aggregated root entity structure
        VehicleService savedService = vehicleServiceRepository.save(service);

        // 8. Broadcast Lifecycle Event for Global State Sync
        // This allows the Vehicle module to immediately transition the bus status to 'UNDER_MAINTENANCE'
        //eventPublisher.publishEvent(new VehicleServiceCreatedEvent(this, savedService));

        return vehicleServiceMapper.toDto(savedService);
    }

    @Transactional
    public VehicleServiceDetailResponseDto startExecution(Integer id, VehicleServiceStartRequestDto dto) {
        VehicleService service = getById(id);
        VehicleServiceStatus targetStatus = vehicleServiceStatusService.getByName("In Progress");

        // 1. Fire state transition logic (Validates if current state allows moving to IN_PROGRESS)
        vehicleServiceStateTransitionHandler.transitionTo(service, targetStatus);

        // 2. Perform Operational Data Write to child Execution table
        VehicleServiceExecution execution = new VehicleServiceExecution();
        execution.setVehicleservice(service);
        execution.setBranch(service.getBranch());
        execution.setDostarted(LocalDate.now());
        execution.setStartodometer(dto.getStartodometer());
        execution.setMaintechnician(employeeService.getById(dto.getMaintechnicianId()));

        vehicleServiceExecutionRepository.save(execution);

        return vehicleServiceMapper.toDto(vehicleServiceRepository.save(service));
    }

    @Transactional
    public VehicleServiceDetailResponseDto placeOnHold(Integer id) {
        VehicleService service = getById(id);
        VehicleServiceStatus targetStatus = vehicleServiceStatusService.getByName("On Hold Parts");

        vehicleServiceStateTransitionHandler.transitionTo(service, targetStatus);

        return vehicleServiceMapper.toDto(vehicleServiceRepository.save(service));
    }

    @Transactional
    public VehicleServiceDetailResponseDto complete(Integer id, VehicleServiceCompleteRequestDto dto) {
        VehicleService service = getById(id);
        VehicleServiceStatus targetStatus = vehicleServiceStatusService.getByName("Completed");
        // 1. Process State Transition
        vehicleServiceStateTransitionHandler.transitionTo(service, targetStatus);

        // 2. Fetch the active open execution row matching this service window
        VehicleServiceExecution activeExecution = vehicleServiceExecutionRepository.findByVehicleserviceAndDoendIsNull(service)
                .orElseThrow(() -> new BusinessRuleViolationException("No active execution segment found to complete"));

        // 3. Map request remarks across safely via MapStruct target merging
        vehicleServiceMapper.updateExecutionWithCompletePayload(dto, activeExecution);

        // 4. System level computation & close out data assignment
        activeExecution.setDoend(LocalDate.now());
        activeExecution.setNextserviceinkm(activeExecution.getStartodometer() + dto.getServiceIntervalKm());

        // 5. Commit history changes to persistence
        vehicleServiceExecutionRepository.save(activeExecution);
        vehicleServiceRepository.save(service);

        // 6. Broadcast event so other modules know this bus is fully cleared for passenger routes
        //eventPublisher.publishEvent(new VehicleServiceCompletedEvent(this, service));

        return vehicleServiceMapper.toDto(service);
    }

    private VehicleService getById(Integer id) {
        return vehicleServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle service ticket not found with id: " + id));
    }
}
