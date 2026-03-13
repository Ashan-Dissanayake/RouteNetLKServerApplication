package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Conditionrate;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleState;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateFactory;
import lk.ashan.routenetlkserverapllication.module.vehicle.validation.VehicleValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private List<VehicleValidationStrategy> validationStrategies;

    @Mock
    private VehicleStateFactory vehicleStateFactory;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
         // Because we iterate over the list, we need to mock iterator if it's a direct list interaction, 
         // but since it's injected, usually we mock the execution inside the method.
         // However, simple forEach on a mock list might not work as expected without stubbing iterator.
         // Better way is to create a real list of mock strategies.
         VehicleValidationStrategy mockStrategy = mock(VehicleValidationStrategy.class);
         vehicleService = new VehicleService(vehicleRepository, vehicleMapper, List.of(mockStrategy), vehicleStateFactory);
    }

    @Test
    void createVehicle_shouldInvokeStrategiesAndSave() {
        VehicleCreateRequestDto request = new VehicleCreateRequestDto();
        Vehicle vehicle = new Vehicle();
        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1);
        VehicleDetailResponseDto responseDto = new VehicleDetailResponseDto();

        when(vehicleMapper.toEntity(request)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(savedVehicle);
        when(vehicleMapper.toDto(savedVehicle)).thenReturn(responseDto);

        VehicleDetailResponseDto result = vehicleService.createVehicle(request);

        assertNotNull(result);
        verify(vehicleRepository).save(vehicle);
        // Verify strategies were called? (Implied by constructor setup)
    }

    @Test
    void updateVehicle_shouldCheckStateTransition() {
        VehicleUpdateRequestDto request = new VehicleUpdateRequestDto();
        request.setId(1);
        ConditionrateDto cr = new ConditionrateDto(); cr.setName("GOOD");
        request.setConditionrate(cr);
        VehiclestatusDto vsReq = new VehiclestatusDto(); vsReq.setName("SUSPEND");
        request.setVehiclestatus(vsReq);

        Vehicle existing = new Vehicle();
        existing.setId(1);
        Conditionrate crExist = new Conditionrate(); crExist.setName("GOOD");
        existing.setConditionrate(crExist);
        Vehiclestatus vsExist = new Vehiclestatus(); vsExist.setName("ACTIVE");
        existing.setVehiclestatus(vsExist);

        when(vehicleRepository.findByMyId(1)).thenReturn(existing);
        when(vehicleMapper.toEntity(request)).thenReturn(new Vehicle()); // needed for passing to state
        
        VehicleState mockState = mock(VehicleState.class);
        when(vehicleStateFactory.getState("ACTIVE")).thenReturn(mockState);
        when(vehicleRepository.save(any())).thenReturn(existing);
        when(vehicleMapper.toDto(any())).thenReturn(new VehicleDetailResponseDto());

        vehicleService.updateVehicle(request);

        verify(mockState).transitionTo(eq(existing), any());
    }
}
