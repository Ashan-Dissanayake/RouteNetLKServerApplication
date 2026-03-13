package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleUniquenessValidationStrategyTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleUniquenessValidationStrategy strategy;

    @Test
    void validateCreate_shouldThrow_whenNumberExists() {
        VehicleCreateRequestDto request = new VehicleCreateRequestDto();
        request.setNumber("TEST-NUMBER");

        when(vehicleRepository.existsByNumber("TEST-NUMBER")).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> strategy.validateCreate(request));
    }
}
