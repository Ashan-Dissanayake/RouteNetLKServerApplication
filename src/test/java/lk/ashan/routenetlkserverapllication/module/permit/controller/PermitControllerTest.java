package lk.ashan.routenetlkserverapllication.module.permit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;


import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/vehicle/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/vehicle/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/permit/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/permit/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PermitControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    private final String API_URL = "/permits";

    @ParameterizedTest
    @MethodSource("missingPermitFieldProvider")
    void createPermit_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PermitCreateRequestDto> mutator
    ) throws Exception {

        PermitCreateRequestDto dto = PermitCreateRequestDto.builder()
                .number("ABC-DE123-456")
                .vehicle(VehicleSummaryDto.builder().id(1).build())
                .doissued(LocalDate.now())
                .doexpired(LocalDate.now().plusDays(1))
                .branch(BranchSummaryDto.builder().id(1).build())
                .permitestatus(PermitStatusDto.builder().id(1).build())
                .servicetype(ServiceTypeDto.builder().id(1).build())
                .route(RouteSummaryRequestDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingPermitFieldProvider() {
        return Stream.of(

                Arguments.of(
                        "number",
                        "Number is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setNumber(null)
                ),

                Arguments.of(
                        "vehicle",
                        "Vehicle is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setVehicle(null)
                ),

                Arguments.of(
                        "doissued",
                        "Date of issued is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setDoissued(null)
                ),

                Arguments.of(
                        "doexpired",
                        "Date of Exp is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setDoexpired(null)
                ),

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "permitestatus",
                        "Permit status is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setPermitestatus(null)
                ),

                Arguments.of(
                        "servicetype",
                        "Service type is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setServicetype(null)
                ),

                Arguments.of(
                        "route",
                        "Route is mandatory",
                        (Consumer<PermitCreateRequestDto>) dto -> dto.setRoute(null)
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "269",                      // only 3 digits
            "26967",                    // 5 digits instead of 4
            "ANG-NA-7845-103",          // extra hyphen after NA
            "ANG-na7845-103",           // lowercase letters
            "ANG-NA78A5-103",           // letter in digit block
            "ANG-NA7845",               // missing numeric suffix
            "ANG-NA7845-",              // trailing hyphen
            "ANG-NA7845-103-",          // trailing hyphen after numbers
            "ANG--NA7845-103",          // double hyphen
            "ANG_NA7845-103",           // underscore instead of hyphen
            "AN-NA7845-103",            // only 2 letters in first block
            "ANG-N7845-103",            // only 1 letter before digits
            " ang-NA7845-103",          // leading space
            "ANG-NA7845-103 ",          // trailing space
            "ANG-NA7845-103\\",         // dangling backslash
            "ANG-NA7845-103\\ang-NA1"   // lowercase in chained permit
    })
    void createPermit_shouldFail_whenPermitNumberFormatIsInvalid(String invalidPermit) throws Exception {
        PermitCreateRequestDto dto = PermitCreateRequestDto.builder()
                .number(invalidPermit)
                .vehicle(VehicleSummaryDto.builder().id(1).build())
                .doissued(LocalDate.now())
                .doexpired(LocalDate.now().plusDays(1))
                .branch(BranchSummaryDto.builder().id(1).build())
                .permitestatus(PermitStatusDto.builder().id(1).build())
                .servicetype(ServiceTypeDto.builder().id(1).build())
                .route(RouteSummaryRequestDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("number: Invalid Permit Number")));
    }

    @Test
    void createPermit_shouldFail_whenPermitNumberAlreadyExists() throws Exception {
        PermitCreateRequestDto dto = PermitCreateRequestDto.builder()
                .number("ANG-NA7845-103-3")
                .vehicle(VehicleSummaryDto.builder().id(1).build())
                .doissued(LocalDate.now())
                .doexpired(LocalDate.now().plusDays(1))
                .branch(BranchSummaryDto.builder().id(1).build())
                .permitestatus(PermitStatusDto.builder().id(1).build())
                .servicetype(ServiceTypeDto.builder().id(1).build())
                .route(RouteSummaryRequestDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem("Permit number already exists.")));
    }

    @Test
    void createPermit_shouldFail_whenPermitStatusIsInvalid() throws Exception {
        PermitCreateRequestDto dto = PermitCreateRequestDto.builder()
                .number("ANG-NA7846-103-3")
                .vehicle(VehicleSummaryDto.builder().id(2).build())
                .doissued(LocalDate.now())
                .doexpired(LocalDate.now().plusDays(1))
                .branch(BranchSummaryDto.builder().id(1).build())
                .permitestatus(PermitStatusDto.builder().id(2).name("Expired").build())
                .servicetype(ServiceTypeDto.builder().id(1).build())
                .route(RouteSummaryRequestDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem(  "This state is not allowed as initial state")));
    }

    @Test
    void createPermit_shouldFail_whenBusTypeAndRouteTypeIsInvalidCombination() throws Exception {
        PermitCreateRequestDto dto = PermitCreateRequestDto.builder()
                .number("1865")
                .vehicle(VehicleSummaryDto.builder().id(3).build())
                .doissued(LocalDate.now())
                .doexpired(LocalDate.now().plusDays(1))
                .branch(BranchSummaryDto.builder().id(1).build())
                .permitestatus(PermitStatusDto.builder().id(2).name("Expired").build())
                .servicetype(ServiceTypeDto.builder().id(1).build())
                .route(RouteSummaryRequestDto.builder().id(2).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem(
                        "Invalid combination: Type C buses cannot be used on Inter provincial route.")
                ));
    }

    @Test
    void transferPermit_shouldTransferPermit_andSetVehicleAvailable() throws Exception {

        Integer permitId = 1;

        PermitTransferRequestDto request =
                PermitTransferRequestDto.builder()
                        .newStatusId(4) // Transferred
                        .build();

        mockMvc.perform(post(API_URL + "/{permitId}/transfer", permitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.data.permitestatus.name")
                        .value("Transferred"));
    }

    @Test
    void transferPermit_shouldSetVehicleStatusToAvailable() throws Exception {

        Integer permitId = 1;

        PermitTransferRequestDto request =
                PermitTransferRequestDto.builder()
                        .newStatusId(4)
                        .build();

        mockMvc.perform(post(API_URL + "/{permitId}/transfer", permitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Vehicle vehicle = vehicleRepository.findById(1).orElseThrow();

        assertEquals(
                "Available",
                vehicle.getVehiclestatus().getName()
        );
    }


    @Test
    void transferPermit_shouldFail_whenPermitNotFound() throws Exception {

        Integer invalidId = 999;

        PermitTransferRequestDto request =
                PermitTransferRequestDto.builder()
                        .newStatusId(4)
                        .build();

        mockMvc.perform(post(API_URL + "/{permitId}/transfer", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isNotFound());
    }

    @Test
    void transferPermit_shouldFail_whenStatusNotFound() throws Exception {

        PermitTransferRequestDto request =
                PermitTransferRequestDto.builder()
                        .newStatusId(999)
                        .build();

        mockMvc.perform(post(API_URL + "/{permitId}/transfer", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isNotFound());
    }

    @Test
    void transferPermit_shouldFail_whenPermitAlreadyTransferred() throws Exception {

        PermitTransferRequestDto request =
                PermitTransferRequestDto.builder()
                        .newStatusId(4)
                        .build();

        mockMvc.perform(post(API_URL + "/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(post(API_URL + "/1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isConflict());
    }
}
