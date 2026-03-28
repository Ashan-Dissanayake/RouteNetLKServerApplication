package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;


import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/vehicle/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/vehicle/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class VehicleControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/vehicles";

    @ParameterizedTest
    @MethodSource("missingVehicleFieldProvider")
    void createVehicle_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<VehicleCreateRequestDto> mutator
    ) throws Exception {

        VehicleCreateRequestDto dto = VehicleCreateRequestDto.builder()
                .number("NA-1234")
                .mileage(10000)
                .remarks("Test vehicle")
                .fueltype(FueltypeDto.builder().id(1).build())
                .conditionrate(ConditionrateDto.builder().id(1).build())
                .vehiclestatus(VehiclestatusDto.builder().id(1).build())
                .branch(BranchSummaryDto.builder().id(1).build())
                .model(ModelDto.builder().id(1).build())
                .bustype(BusTypeDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingVehicleFieldProvider() {
        return Stream.of(

                Arguments.of(
                        "number",
                        "Plate Number Can Not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setNumber(null)
                ),

                Arguments.of(
                        "mileage",
                        "Mileage Can Not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setMileage(null)
                ),
                Arguments.of(
                        "fueltype",
                        "Fuel Type Can Not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setFueltype(null)
                ),

                Arguments.of(
                        "conditionrate",
                        "Condition Rate Can Not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setConditionrate(null)
                ),

                Arguments.of(
                        "vehiclestatus",
                        "Vehicle Status Can Not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setVehiclestatus(null)
                ),

                Arguments.of(
                        "branch",
                        "Branch Can Not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "model",
                        "Model can not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setModel(null)
                ),
                Arguments.of(
                        "bustype",
                        "Bus type can not be Empty",
                        (Consumer<VehicleRequestDto>) dto -> dto.setBustype(null)
                )
        );
    }


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

        VehicleCreateRequestDto dto = VehicleCreateRequestDto.builder()
                .number(invalidNumber)
                .mileage(10000)
                .remarks("Test vehicle")
                .fueltype(FueltypeDto.builder().id(1).build())
                .conditionrate(ConditionrateDto.builder().id(1).build())
                .vehiclestatus(VehiclestatusDto.builder().id(1).build())
                .branch(BranchSummaryDto.builder().id(1).build())
                .model(ModelDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("number: Invalid Plate Number")));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1,            // negative
            10000000,      // 8 digits
            100000000      // 9 digits
    })
    void createVehicle_shouldFail_whenMileageIsInvalid(int invalidMileage) throws Exception {
        VehicleCreateRequestDto dto = VehicleCreateRequestDto.builder()
                .number("NA-1234")
                .mileage(invalidMileage)
                .remarks("Test vehicle")
                .fueltype(FueltypeDto.builder().id(1).build())
                .conditionrate(ConditionrateDto.builder().id(1).build())
                .vehiclestatus(VehiclestatusDto.builder().id(1).build())
                .branch(BranchSummaryDto.builder().id(1).build())
                .model(ModelDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem
                        (invalidMileage < 0 ? "mileage: Mileage must be positive" :
                        "mileage: Numeric value out of bounds (<7 digits> expected)"))
                );
    }

    @Test
    void createVehicle_shouldFail_whenNumberIsExist() throws Exception{
        VehicleCreateRequestDto dto = VehicleCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(1).build())
                .number("ND-1217")
                .model(ModelDto.builder().id(1).build())
                .bustype(BusTypeDto.builder().id(1).build())
                .mileage(10000)
                .fueltype(FueltypeDto.builder().id(1).build())
                .conditionrate(ConditionrateDto.builder().id(1).build())
                .vehiclestatus(VehiclestatusDto.builder().id(1).build())
                .remarks("Test vehicle")
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem("Vehicle number already exists.")));
    }


    @ParameterizedTest
    @MethodSource("vehicleStatusTransitionProvider")
    void updateVehicle_statusTransitionValidation(
            VehiclestatusDto newStatus,
            String expectedErrorMessage,
            ResultMatcher expectedStatus
    ) throws Exception {
        VehicleUpdateRequestDto dto = VehicleUpdateRequestDto.builder()
                .id(1)
                .branch(BranchSummaryDto.builder().id(1).build())
                .number("ND-1217")
                .model(ModelDto.builder().id(1).build())
                .bustype(BusTypeDto.builder().id(1).build())
                .mileage(11000)
                .fueltype(FueltypeDto.builder().id(1).build())
                .conditionrate(ConditionrateDto.builder().id(2).name("Good").build())
                .vehiclestatus(VehiclestatusDto.builder().id(1).build())
                .remarks("Test vehicle")
                .build();
        dto.setVehiclestatus(newStatus);

        ResultActions result = mockMvc.perform(put(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        result.andExpect(expectedStatus);

        if (expectedErrorMessage != null) {
            result.andExpect(jsonPath("$.details",hasItem(expectedErrorMessage)));
        }
    }

    private static Stream<Arguments> vehicleStatusTransitionProvider() {

        return Stream.of(

                Arguments.of(
                        VehiclestatusDto.builder().id(4).name("Out Of Service").build(),
                        "Invalid status transition from AVAILABLE to OUT OF SERVICE",
                        status().isConflict()
                ),

                Arguments.of(
                        VehiclestatusDto.builder().id(5).name("Decommissioned").build(),
                        "Invalid status transition from AVAILABLE to DECOMMISSIONED",
                        status().isConflict()
                ),

                Arguments.of(
                        VehiclestatusDto.builder().id(6).name("Reserved").build(),
                        null,
                        status().isOk()
                )
        );
    }
}
