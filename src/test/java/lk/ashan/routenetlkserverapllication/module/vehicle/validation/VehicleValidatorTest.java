package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleValidatorTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleValidator validator;

    // =========================================================
    // CREATE - VEHICLE NUMBER
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenVehicleNumberAlreadyExists() {

        VehicleCreateRequestDto request =
                mock(VehicleCreateRequestDto.class);

        when(request.getNumber()).thenReturn("NB-1234");
        when(vehicleRepository.existsByNumber("NB-1234"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(vehicleRepository)
                .existsByNumber("NB-1234");
    }

    @Test
    void validateCreate_shouldPassWhenVehicleNumberDoesNotExist() {

        VehicleCreateRequestDto request =
                mock(VehicleCreateRequestDto.class);

        when(request.getNumber()).thenReturn("NB-1234");
        when(vehicleRepository.existsByNumber("NB-1234"))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );

        verify(vehicleRepository)
                .existsByNumber("NB-1234");
    }

    // =========================================================
    // UPDATE - MILEAGE
    // =========================================================

    @Test
    void validateUpdate_shouldThrowExceptionWhenNewMileageIsLessThanCurrentMileage() {

        Vehicle existingVehicle = mock(Vehicle.class);
        VehicleUpdateRequestDto request =
                mock(VehicleUpdateRequestDto.class, RETURNS_DEEP_STUBS);

        when(existingVehicle.getMileage()).thenReturn(50000);
        when(request.getMileage()).thenReturn(49000);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenNewMileageIsGreaterThanCurrentMileage() {

        Vehicle existingVehicle = mock(Vehicle.class);
        VehicleUpdateRequestDto request =
                mock(VehicleUpdateRequestDto.class, RETURNS_DEEP_STUBS);

        when(existingVehicle.getMileage()).thenReturn(50000);
        when(request.getMileage()).thenReturn(51000);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenNewMileageIsEqualToCurrentMileage() {

        Vehicle existingVehicle = mock(Vehicle.class);
        VehicleUpdateRequestDto request =
                mock(VehicleUpdateRequestDto.class, RETURNS_DEEP_STUBS);

        when(existingVehicle.getMileage()).thenReturn(50000);
        when(request.getMileage()).thenReturn(50000);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenNewMileageIsNull() {

        Vehicle existingVehicle = mock(Vehicle.class);
        VehicleUpdateRequestDto request =
                mock(VehicleUpdateRequestDto.class, RETURNS_DEEP_STUBS);

        when(existingVehicle.getMileage()).thenReturn(50000);
        when(request.getMileage()).thenReturn(null);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenCurrentMileageIsNull() {

        Vehicle existingVehicle = mock(Vehicle.class);
        VehicleUpdateRequestDto request =
                mock(VehicleUpdateRequestDto.class, RETURNS_DEEP_STUBS);

        when(existingVehicle.getMileage()).thenReturn(null);
        when(request.getMileage()).thenReturn(50000);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    // =========================================================
    // UPDATE - CONDITION RATE
    // =========================================================

    @Test
    void validateUpdate_shouldAllowExcellentToGoodTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("EXCELLENT");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("GOOD");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldAllowGoodToFairTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("GOOD");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("FAIR");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldAllowFairToPoorTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("FAIR");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("POOR");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldAllowPoorToCriticalTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("POOR");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("CRITICAL");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldAllowSameConditionRate() {

        Vehicle existingVehicle =
                createVehicleWithCondition("GOOD");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("GOOD");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    // =========================================================
    // UPDATE - INVALID CONDITION RATE TRANSITIONS
    // =========================================================

    @Test
    void validateUpdate_shouldRejectExcellentToFairTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("EXCELLENT");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("FAIR");

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldRejectExcellentToPoorTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("EXCELLENT");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("POOR");

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldRejectExcellentToCriticalTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("EXCELLENT");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("CRITICAL");

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldRejectBackwardConditionTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("FAIR");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("GOOD");

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldRejectCriticalToPoorTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("CRITICAL");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("POOR");

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldRejectCriticalToExcellentTransition() {

        Vehicle existingVehicle =
                createVehicleWithCondition("CRITICAL");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("EXCELLENT");

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    // =========================================================
    // UPDATE - CONDITION RATE EDGE CASES
    // =========================================================

    @Test
    void validateUpdate_shouldThrowExceptionWhenCurrentConditionRateIsNull() {

        Vehicle existingVehicle =
                createVehicleWithCondition(null);

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("GOOD");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldSkipConditionValidationWhenNewConditionRateIsNull() {

        Vehicle existingVehicle = mock(Vehicle.class);

        VehicleUpdateRequestDto request =
                mock(VehicleUpdateRequestDto.class, RETURNS_DEEP_STUBS);

        when(request.getConditionrate()).thenReturn(null);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenCurrentConditionRateIsUnknown() {

        Vehicle existingVehicle =
                createVehicleWithCondition("UNKNOWN");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("GOOD");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldHandleConditionRateWithDifferentCase() {

        Vehicle existingVehicle =
                createVehicleWithCondition("excellent");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("good");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldHandleConditionRateWithWhitespace() {

        Vehicle existingVehicle =
                createVehicleWithCondition("  EXCELLENT  ");

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("  GOOD  ");

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    // =========================================================
    // UPDATE - COMBINED VALIDATION
    // =========================================================

    @Test
    void validateUpdate_shouldPassWhenMileageAndConditionAreBothValid() {

        Vehicle existingVehicle =
                createVehicleWithCondition("GOOD");

        when(existingVehicle.getMileage()).thenReturn(50000);

        VehicleUpdateRequestDto request =
                createUpdateRequestWithCondition("FAIR");

        when(request.getMileage()).thenReturn(51000);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validateUpdate_shouldThrowMileageExceptionBeforeConditionValidation() {

        Vehicle existingVehicle = mock(Vehicle.class);

        VehicleUpdateRequestDto request = mock(VehicleUpdateRequestDto.class);

        when(existingVehicle.getMileage()).thenReturn(50000);
        when(request.getMileage()).thenReturn(49000);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existingVehicle, request)
        );

        verifyNoInteractions(vehicleRepository);
    }

    // =========================================================
    // TEST FIXTURES
    // =========================================================

    private Vehicle createVehicleWithCondition(String conditionRate) {

        Vehicle vehicle = mock(Vehicle.class, RETURNS_DEEP_STUBS);

        when(vehicle.getConditionrate().getName())
                .thenReturn(conditionRate);

        return vehicle;
    }

    private VehicleUpdateRequestDto createUpdateRequestWithCondition(
            String conditionRate) {

        VehicleUpdateRequestDto request =
                mock(
                        VehicleUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getConditionrate().getName())
                .thenReturn(conditionRate);

        return request;
    }
}