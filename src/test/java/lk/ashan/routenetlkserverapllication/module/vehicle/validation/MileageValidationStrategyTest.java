package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidMileageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MileageValidationStrategyTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private MileageValidationStrategy strategy;

    @Test
    void validateUpdate_shouldThrow_whenMileageDecreases() {
        VehicleUpdateRequestDto request = new VehicleUpdateRequestDto();
        request.setId(1);
        request.setMileage(5000);

        Vehicle existing = new Vehicle();
        existing.setMileage(6000);

        when(vehicleRepository.findByMyId(1)).thenReturn(existing);

        assertThrows(InvalidMileageException.class, () -> strategy.validateUpdate(request));
    }

    @Test
    void validateUpdate_shouldPass_whenMileageIncreases() {
        VehicleUpdateRequestDto request = new VehicleUpdateRequestDto();
        request.setId(1);
        request.setMileage(7000);

        Vehicle existing = new Vehicle();
        existing.setMileage(6000);

        when(vehicleRepository.findByMyId(1)).thenReturn(existing);

        assertDoesNotThrow(() -> strategy.validateUpdate(request));
    }
}
