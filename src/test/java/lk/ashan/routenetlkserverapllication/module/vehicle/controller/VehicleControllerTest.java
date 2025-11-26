package lk.ashan.routenetlkserverapllication.module.vehicle.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.util.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.util.factory.VehicleDtoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;

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

    //Mandatory Attributes
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

    @Test
    void createVehicle_shouldFail_whenMileageIsMissing() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setMileage(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mileage: Mileage Can Not be Empty"
                ));
    }


    //Pattern and format validations

    //Code
    @ParameterizedTest
    @ValueSource(strings = {
            "ALV00013",      // missing prefix
            "AB-ALV00013",   // wrong prefix
            "BSS-ALV00013",  // too long prefix
            "BSALV00013",    // missing hyphen
            "BS-alv00013",   // lowercase letters
            "bs-ALV00013",   // lowercase prefix
            "BS-AL000013",   // 2 letters
            "BS-ALVV00013",  // 4 letters
            "BS-A1V00013",   // digit in letter block
            "BS-ALV123",     // too few digits
            "BS-ALV123456",  // too many digits
            "BS-ALV12A45",   // letter in digit block
            "BS-ALV00013XYZ",// trailing chars
            " BS-ALV00013",  // leading space
            "BS-ALV00013 "   // trailing space
    })
    void createVehicle_shouldFail_whenCodeFormatIsInvalid(String invalidCode) throws Exception {

        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setCode(invalidCode);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "code: Invalid Code"
                ));
    }

    //Number
    @ParameterizedTest
    @ValueSource(strings = {
            "A-1234",       // missing N prefix
            "NB1234",       // missing hyphen
            "NAB-1234",     // too many letters
            "N1-1234",      // digit in letter part
            "Na-1234",      // lowercase
            "NA-123",       // too few digits
            "NA-12345",     // too many digits
            "NA-12A4",      // letter in digit block
            "NA-1234XYZ",   // trailing invalid chars
            " NA-1234",     // leading space
            "NA-1234 "      // trailing space
    })
    void createVehicle_shouldFail_whenPlateNumberFormatIsInvalid(String invalidNumber) throws Exception {

        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setNumber(invalidNumber);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid Plate Number"
                ));
    }

    //Date of Buy
    @Test
    void createVehicle_shouldFail_whenDobIsInFuture() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setDob(LocalDate.now().plusDays(1));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "dob: DOB Date Can Not be in the Future"
                ));
    }


    //Year of Made
    @Test
    void createVehicle_shouldFail_whenYomIsInFuture() throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setYom(Year.now().plusYears(1));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "yom: YOM Date Can Not be in the Future"
                ));
    }

    //Mileage
    @ParameterizedTest
    @ValueSource(ints = {
            -1,            // negative
            10000000,      // 8 digits
            100000000      // 9 digits
    })
    void createVehicle_shouldFail_whenMileageIsInvalid(int invalidMileage) throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setMileage(invalidMileage);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        invalidMileage < 0 ? "mileage: Mileage must be positive" :
                                "mileage: Numeric value out of bounds (<7 digits> expected)"
                ));
    }


}
