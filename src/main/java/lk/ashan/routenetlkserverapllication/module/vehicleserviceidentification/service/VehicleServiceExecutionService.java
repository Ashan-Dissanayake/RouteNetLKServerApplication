package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServiceSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper.VehicleServiceMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServiceExecution;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceExecutionRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceStatusRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state.VehicleServiceStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class VehicleServiceExecutionService {

    private final VehicleServiceExecutionRepository vehicleServiceExecutionRepository;
    private final VehicleServiceStatusRepository vehicleServiceStatusRepository;
    private final VehicleServiceStateTransitionHandler vehicleServiceStateTransitionHandler;
    private final VehicleServiceMapper vehicleServiceMapper;

    @Transactional
    public VehicleServiceSummaryResponseDto startService(@NotNull Integer scheduleId){
        VehicleServiceExecution schedule =
                vehicleServiceExecutionRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if(schedule.getDostarted() != null){
            throw new BusinessRuleViolationException("Service already started");
        }

        VehicleService service = schedule.getVehicleservice();

        String currentStatus = service.getVehicleservicestatus().getName();

        if (!currentStatus.equalsIgnoreCase("SCHEDULED")) {
            throw new BusinessRuleViolationException(
                    "Invalid status transition. Expected: " +
                            "SCHEDULED but found: " + currentStatus
            );
        }

        VehicleServiceStatus inProgress =
                vehicleServiceStatusRepository.findByName("In progress");

        vehicleServiceStateTransitionHandler.transitionTo(service,inProgress);

        return vehicleServiceMapper.toDto(service);
    }

    @Transactional
    public VehicleServiceSummaryResponseDto completeService(Integer scheduleId){
        VehicleServiceExecution schedule =
                vehicleServiceExecutionRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if(schedule.getDoend() != null){
            throw new BusinessRuleViolationException("Service already completed");
        }

        VehicleService service = schedule.getVehicleservice();

        String currentStatus = service.getVehicleservicestatus().getName();

        if (!currentStatus.equalsIgnoreCase("IN PROGRESS")) {
            throw new BusinessRuleViolationException(
                    "Invalid status transition. Expected: " +
                            "IN PROGRESS but found: " + currentStatus
            );
        }


        VehicleServiceStatus inProgress =
                vehicleServiceStatusRepository.findByName("Completed");

        vehicleServiceStateTransitionHandler.transitionTo(service,inProgress);

        return vehicleServiceMapper.toDto(service);

    }
}
