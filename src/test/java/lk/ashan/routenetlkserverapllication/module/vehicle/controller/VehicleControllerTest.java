package lk.ashan.routenetlkserverapllication.module.vehicle.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed.VehicleValidationData;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.VehicleDtoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    void createVehicle_shouldSucceed_whenUniqueValid_vehicleCreateRequest() throws Exception {

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

    //Chassis Number
    @ParameterizedTest
    @CsvSource({
            "Ashok Leyland Viking 193, ALV12345678901234",  // valid
            "Ashok Leyland Viking 193, A!V12345678901234",  // invalid char
            "Tata LP 12.10/42, TLP12345678901234",          // valid
            "Tata LP 12.10/42, TLP1234567890",             // too short
            "Isuzu MT 111L, ISUZU1234567890123",           // valid
            "Isuzu MT 111L, ISUZU1234567890$",             // invalid char
    })
    void createVehicle_shouldValidateChassis(String model, String chassisnumber) throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.getMake().setName(model);
        dto.setChasisnumber(chassisnumber);

        ResultActions result = mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        if (chassisnumber.matches(VehicleValidationData.CHASSIS_REGEX.get(model))) {
            result.andExpect(status().isCreated());
        } else {
            result.andExpect(status().isBadRequest())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "chasisnumber: Invalid chassis number for " + dto.getMake().getName()
                    ));
        }
    }

    //Engine Number
    @ParameterizedTest
    @CsvSource({
            "Ashok Leyland Viking 193, ABC123DEF456",       // valid for ^[A-Z0-9]{12}$
            "Ashok Leyland Viking 193, ABC123DEF45!",       // invalid char
            "Ashok Leyland Viking 210 Turbo, ABC123456XYZ",// valid for ^[A-Z0-9]{3}[0-9]{3}[A-Z0-9]{5}$
            "Ashok Leyland Viking 210 Turbo, AB!123456XYZ",// invalid char
            "Leyland Tiger TL 11, AB123CC.123456",         // valid for ^[A-Z]{2}[0-9]{3}[A-Z]{2}\.[0-9]{6}$
            "Leyland Tiger TL 11, AB123C1.12345",          // too short
    })
    void createVehicle_shouldValidateEngine(String model, String engineNumber) throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.getMake().setName(model);
        dto.setEnginenumber(engineNumber);

        ResultActions result = mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        if (engineNumber.matches(VehicleValidationData.ENGINE_REGEX.get(model))) {
            result.andExpect(status().isCreated());
        } else {
            result.andExpect(status().isBadRequest())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "enginenumber: Invalid engine number for " + dto.getMake().getName()
                    ));
        }
    }

    //Uniqueness
    @Test
    void createVehicle_shouldFail_whenCodeIsExist() throws Exception{
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
       dto.setCode("BS-ALV00001");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Vehicle code already exists."
                ));
    }

    @Test
    void createVehicle_shouldFail_whenNumberIsExist() throws Exception{
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setNumber("ND-1217");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Vehicle number already exists."
                ));
    }

    @Test
    void createVehicle_shouldFail_whenChasisnumberIsExist() throws Exception{
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.setChasisnumber("KLWT712345ABC6789");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Vehicle chassis number already exists."
                ));
    }

    @Test
    void createVehicle_shouldFail_whenEnginenumberIsExist() throws Exception{
            VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
            dto.setEnginenumber("VLK193A1B2C3");

            mockMvc.perform(post(apiUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isConflict())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "Vehicle engine number already exists."
                    ));
        }

    //Cross field validation
    @ParameterizedTest
    @CsvSource({
            "Ashok Leyland Viking 193, ALV12345678901234, true",   // correct model → valid
            "Ashok Leyland Viking 210 Turbo, ABC123456XYZ, false",  // wrong model → cross-field fail
            "Tata LP 12.10/42, TLP12345678901277, true",             // correct model
            "Tata LP 15.10/52, ALV123456789012, false"             // valid format but wrong model → fail
    })
    void createVehicle_shouldValidateModelChassis(String model, String chassis, boolean valid) throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.getMake().setName(model);
        dto.setChasisnumber(chassis);

        ResultActions result = mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        if (valid) {
            result.andExpect(status().isCreated());
        } else {
            result.andExpect(status().isBadRequest())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "chasisnumber: Invalid chassis number for " + dto.getMake().getName()
                    ));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "Ashok Leyland Viking 193, ABC123DEF456, true",   // valid model → engine matches model
            "Leyland Tiger TL 11, AV123CD.123456, true",      // valid model → engine matches
    })
    void createVehicle_shouldValidateModelEngine(String model, String engineNumber, boolean valid) throws Exception {
        VehicleCreateRequestDto dto = VehicleDtoFactory.createUniqueVehicleRequest();
        dto.getMake().setName(model);
        dto.setEnginenumber(engineNumber);

        ResultActions result = mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        if (valid) {
            result.andExpect(status().isCreated());
        } else {
            result.andExpect(status().isBadRequest())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "enginenumber: Engine number does not match model"
                    ));
        }
    }

    //Status Transition Validation in Update Process
    @Test
    void updateVehicle_shouldFail_whenStatusFromAvailableToOutOfService() throws Exception {
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setVehiclestatus(VehicleDtoFactory.vehiclestatusDto(4,"Out Of Service")); // Not allowed in transition map

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid status transition from AVAILABLE to OUT OF SERVICE"
                ));
    }

    //Status Transition Validation in Update Process
    @Test
    void updateVehicle_shouldFail_whenStatusFromAvailableToDecommistioned() throws Exception {
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setVehiclestatus(VehicleDtoFactory.vehiclestatusDto(5,"Decommissioned")); // Not allowed in transition map

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid status transition from AVAILABLE to DECOMMISSIONED"
                ));
    }

    //Status Transition  Validation
    @Test
    void updateVehicle_shouldPass_whenStatusFromAvailableToReserved() throws Exception{
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setVehiclestatus(VehicleDtoFactory.vehiclestatusDto(6,"Reserved"));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isOk());
    }

    //Condition Rate Transition Validation
    @Test
    void updateVehicle_shouldFail_whenConditionRateFromGoodToPoor()throws Exception{
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setConditionrate(VehicleDtoFactory.conditionrateDto(4,"Poor"));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid Rate transition from GOOD to POOR"
                ));
    }

    //Condition Rate Transition Validation
    @Test
    void updateVehicle_shouldPass_whenConditionRateFromGoodToFair()throws Exception{
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setConditionrate(VehicleDtoFactory.conditionrateDto(3,"Fair"));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isOk());
    }

    //Mileage Increase Validation
    @Test void updateVehicle_shouldFail_whenMileageIsDecreased() throws Exception{
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setMileage(4654);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Mileage can not be Minus value"
                ));
    }

    @Test void updateVehicle_shouldPass_whenMileageIsIncreased() throws Exception{
        VehicleUpdateRequestDto vehicleUpdateRequestDto = VehicleDtoFactory.createUniqueVehicleUpdateRequest();
        vehicleUpdateRequestDto.setMileage(9654);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleUpdateRequestDto)))
                .andExpect(status().isOk());
    }

    //Activation,Deactivation
    @Test
    void deactivateVehicle_shouldPass_whenVehiclesExist() throws Exception {

        List<Integer> ids = List.of(23, 24);

        mockMvc.perform(post(apiUrl+"/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deactivateVehicle_shouldFail_whenVehiclesNotFound() throws Exception {

        List<Integer> ids = List.of(99, 100);


        mockMvc.perform(post(apiUrl+"/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "No vehicles found for the given IDs"
                ));
    }

    @Test
    void deactivateVehicle_shouldFail_whenIdsListIsEmpty() throws Exception {

        List<Integer> ids = Collections.emptyList();


        mockMvc.perform(post(apiUrl+"/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "No vehicles found for the given IDs"
                ));
    }

    @Test
    void activateVehicle_shouldPass_whenVehiclesExist() throws Exception {

        List<Integer> ids = List.of(13, 14);

        mockMvc.perform(post(apiUrl+"/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isCreated());
    }


}
