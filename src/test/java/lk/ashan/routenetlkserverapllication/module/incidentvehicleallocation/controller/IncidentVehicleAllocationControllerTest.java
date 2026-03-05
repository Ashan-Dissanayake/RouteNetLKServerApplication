package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto.IncidentVehicleAllocationTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.stream.Stream;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = "spring.sql.init.mode=never")
class IncidentVehicleAllocationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/incident-vehicle-allocations";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createIncidentVehicleAllocation_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<IncidentVehicleAllocationCreateRequestDto> mutator) throws Exception {
        IncidentVehicleAllocationCreateRequestDto dto =IncidentVehicleAllocationCreateRequestDto.builder()
                        .incident(IncidentSummaryResponseDto.builder().id(1).build())
                        .vehicle(VehicleSummaryResponseDto.builder().id(1).build())
                        .providebranch(BranchSummaryResponseDto.builder().id(1).build())
                        .incidentvehicleallocationtype(IncidentVehicleAllocationTypeDto.builder().id(1).build())
                        .incidentvehicleallocationstatus(IncidentVehicleAllocationStatusDto.builder().id(1).build())
                        .doassigned(LocalDateTime.now())
                        .doreleased(LocalDateTime.now().plusHours(1))
                .build();
        mutator.accept(dto);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        field + ": " + errorMessage
                ));
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(
                Arguments.of("incident", "Incident is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setIncident(null)),
                Arguments.of("vehicle", "Vehicle is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setVehicle(null)),
                Arguments.of("providebranch", "Branch is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setProvidebranch(null)),
                Arguments.of("incidentvehicleallocationtype", "Type is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setIncidentvehicleallocationtype(null)),
                Arguments.of("incidentvehicleallocationstatus", "Status is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setIncidentvehicleallocationstatus(null)),
                Arguments.of("doassigned", "Assigned date is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setDoassigned(null)),
                Arguments.of("doreleased", "Released date is mandatory", (Consumer<IncidentVehicleAllocationCreateRequestDto>) dto -> dto.setDoreleased(null))
        );
    }

    @Test
    @Sql(scripts ={"/schema-incident-vehicle-allocation.sql","/data-incident-vehicle-allocation.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createIncidentVehicleAllocation_shouldSucceed() throws Exception {
        // Given: Valid allocation request
                IncidentVehicleAllocationCreateRequestDto requestDto =
                        IncidentVehicleAllocationCreateRequestDto.builder()
                .incident(IncidentSummaryResponseDto.builder().id(1).build())
                .vehicle(VehicleSummaryResponseDto.builder().id(1).build())
                .providebranch(BranchSummaryResponseDto.builder().id(1).build())
                .incidentvehicleallocationtype(IncidentVehicleAllocationTypeDto.builder().id(1).build())
                .incidentvehicleallocationstatus(IncidentVehicleAllocationStatusDto.builder().id(1).build())
                .doassigned(LocalDateTime.now())
                .doreleased(LocalDateTime.now().plusHours(1))
                .build();

        // When: POST create allocation
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // Then: Created successfully
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.status").value("Assigned"))
                .andExpect(jsonPath("$.data.allocationType").value("Primary"))
                .andExpect(jsonPath("$.data.vehicleId").value(1))
                .andExpect(jsonPath("$.data.incidentId").value(1))
                .andExpect(jsonPath("$.meta.status").value("created"));
    }

}
