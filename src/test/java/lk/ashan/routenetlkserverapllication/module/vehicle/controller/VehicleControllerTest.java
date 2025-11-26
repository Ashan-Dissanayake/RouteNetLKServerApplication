package lk.ashan.routenetlkserverapllication.module.vehicle.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.util.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.util.factory.VehicleDtoFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VehicleControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/vehicles";

    @Test
    void createVehicle_shouldSucceed_whenUniqueValid_vehicleCreateRequest() throws Exception{

        VehicleCreateRequestDto createRequestDto = VehicleDtoFactory.createUniqueVehicleRequest();

        mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated());

    }

    @Test
    void createVehicle_shouldFail_whenCodeIsMissing() throws Exception{

        VehicleCreateRequestDto createRequestDto = VehicleDtoFactory.createUniqueVehicleRequest();
        createRequestDto.setCode(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "code: Code Can Not be Empty"
                ));

    }

    @Test
    void createVehicle_shouldFail_whenNumberIsMissing() throws Exception{

        VehicleCreateRequestDto createRequestDto = VehicleDtoFactory.createUniqueVehicleRequest();
        createRequestDto.setNumber(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Plate Number Can Not be Empty"
                ));

    }

    @Test
    void createVehicle_shouldFail_whenSeatingCapacityIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setSeatingcapacity(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "seatingcapacity: Seating Capacity Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenYomIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setYom(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "yom: YOM Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenDobIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setDob(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "dob: DOB Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenMakeIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setMake(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "make: Make/Mode Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenFuelTypeIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setFueltype(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "fueltype: Fuel Type Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenConditionRateIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setConditionrate(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "conditionrate: Condition Rate Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenServiceTypeIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setServicetype(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "servicetype: Service Type Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenVehicleStatusIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setVehiclestatus(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "vehiclestatus: Vehicle Status Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenEmployeeIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setEmployee(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "employee: Employee Can Not be Empty"
                ));
    }

    @Test
    void createVehicle_shouldFail_whenBranchIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setBranch(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branch: Branch Not be Empty"
                ));
    }

}
