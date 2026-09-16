package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VehicleValidator {

    private static final Map<String, List<String>> VALID_CONDITION_TRANSITIONS = Map.of(
            "EXCELLENT", List.of("GOOD"),
            "GOOD", List.of("FAIR"),
            "FAIR", List.of("POOR"),
            "POOR", List.of("CRITICAL"),
            "CRITICAL", List.of()
    );

    private final VehicleRepository vehicleRepository;

    public void validateCreate(VehicleCreateRequestDto request) {
        validateVehicleNumber(request.getNumber());
    }

    public void validateUpdate(Vehicle existingVehicle, VehicleUpdateRequestDto request) {

        validateMileage(existingVehicle.getMileage(), request.getMileage()
        );

        if (request.getConditionrate() != null && request.getConditionrate().getName() != null) {

            validateConditionRateTransition(
                    existingVehicle.getConditionrate().getName(),
                    request.getConditionrate().getName()
            );
        }
    }

    private void validateVehicleNumber(String number) {

        if (vehicleRepository.existsByNumber(number)) {
            throw new ResourceExistsException(
                    "Vehicle number already exists."
            );
        }
    }

    private void validateMileage(Integer currentMileage, Integer newMileage) {

        if (newMileage != null && currentMileage != null && newMileage < currentMileage) {
            throw new BusinessRuleViolationException(
                    "Mileage cannot be less than current value."
            );
        }
    }

    private void validateConditionRateTransition(String currentRate, String newRate) {

        if (currentRate == null || newRate == null) {
            throw new IllegalArgumentException(
                    "Rate cannot be null."
            );
        }

        currentRate = currentRate.trim().toUpperCase();
        newRate = newRate.trim().toUpperCase();

        if (currentRate.equals(newRate)) {
            return;
        }

        List<String> allowedRates = VALID_CONDITION_TRANSITIONS.get(currentRate);

        if (allowedRates == null) {
            throw new IllegalArgumentException(
                    "Unknown current Rate: " + currentRate
            );
        }

        if (!allowedRates.contains(newRate)) {
            throw new InvalidStateTransitionException(
                    "Invalid Rate transition from "
                            + currentRate
                            + " to "
                            + newRate
            );
        }
    }
}