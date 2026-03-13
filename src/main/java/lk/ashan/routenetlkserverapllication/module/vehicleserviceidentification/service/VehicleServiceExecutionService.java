package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServiceSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper.VehicleServiceMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleserviceschedule;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceScheduleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceStatusRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state.VehicleServiceStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class VehicleServiceExecutionService {

    private final VehicleServiceScheduleRepository vehicleServiceScheduleRepository;
    private final VehicleServiceStatusRepository vehicleServiceStatusRepository;
    private final VehicleServiceStateTransitionHandler vehicleServiceStateTransitionHandler;
    private final VehicleServiceMapper vehicleServiceMapper;

    @Transactional
    public VehicleServiceSummaryResponseDto startService(@NotNull Integer scheduleId){
        Vehicleserviceschedule schedule =
                vehicleServiceScheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if(schedule.getDoactualstarted() != null){
            throw new BusinessRuleViolationException("Service already started");
        }

        Vehicleservice service = schedule.getVehicleservice();

        String currentStatus = service.getVehicleservicestatus().getName();

        if (!currentStatus.equalsIgnoreCase("SCHEDULED")) {
            throw new BusinessRuleViolationException(
                    "Invalid status transition. Expected: " +
                            "SCHEDULED but found: " + currentStatus
            );
        }

        schedule.setDoactualstarted(LocalDate.now());

        Vehicleservicestatus inProgress =
                vehicleServiceStatusRepository.findByName("In progress");

        vehicleServiceStateTransitionHandler.transitionTo(service,inProgress);

        return vehicleServiceMapper.toDto(service);
    }

    @Transactional
    public VehicleServiceSummaryResponseDto completeService(Integer scheduleId){
        Vehicleserviceschedule schedule =
                vehicleServiceScheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if(schedule.getDoactualend() != null){
            throw new BusinessRuleViolationException("Service already completed");
        }

        Vehicleservice service = schedule.getVehicleservice();

        String currentStatus = service.getVehicleservicestatus().getName();

        if (!currentStatus.equalsIgnoreCase("IN PROGRESS")) {
            throw new BusinessRuleViolationException(
                    "Invalid status transition. Expected: " +
                            "IN PROGRESS but found: " + currentStatus
            );
        }

        schedule.setDoactualend(LocalDate.now());

        Vehicleservicestatus inProgress =
                vehicleServiceStatusRepository.findByName("Completed");

        vehicleServiceStateTransitionHandler.transitionTo(service,inProgress);

        return vehicleServiceMapper.toDto(service);

    }
}
