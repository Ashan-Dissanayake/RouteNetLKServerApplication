package lk.ashan.routenetlkserverapllication.module.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/employee/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/employee/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/crew/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/crew/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ConductorControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_URL = "/conductors";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createConductor_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<ConductorRequestDto> mutator) throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.parse("2025-06-27"))
                .domedicalexpired(LocalDate.parse("2025-12-27"))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(

                Arguments.of(
                        "domedicalissued",
                        "Medical issued date is mandatory",
                        (Consumer<ConductorRequestDto>)
                                dto -> dto.setDomedicalissued(null)
                ),

                Arguments.of(
                        "domedicalexpired",
                        "Medical expired date is mandatory",
                        (Consumer<ConductorRequestDto>)
                                dto -> dto.setDomedicalexpired(null)
                ),

                Arguments.of(
                        "crewstatus",
                        "Crew status is mandatory",
                        (Consumer<ConductorRequestDto>)
                                dto -> dto.setCrewstatus(null)
                ),

                Arguments.of(
                        "routefamiliaritylevel",
                        "Route Familiarity Level is mandatory",
                        (Consumer<ConductorRequestDto>)
                                dto -> dto.setRoutefamiliaritylevel(null)
                ),

                Arguments.of(
                        "employee",
                        "Employee is mandatory",
                        (Consumer<ConductorRequestDto>)
                                dto -> dto.setEmployee(null)
                )
        );
    }

    @Test
    void createConductor_shouldSucceed_whenValid() throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-07-27"))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();


        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createConductor_shouldFail_whenCrewStatusNotEligible() throws Exception {
        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-07-27"))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(3).name("Active").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem("New conductor must have status 'ELIGIBLE'")));
    }


    @Test
    void createConductor_shouldFail_whenRouteLevelNotLow() throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-06-27"))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(2).name("Medium").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem("New conductor route familiarity must have 'LOW'")));
    }

    @Test
    void createConductor_shouldFail_whenMedicalExpiryPast() throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.parse("2025-06-27"))
                .domedicalexpired(LocalDate.now().minusDays(1))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("Medical expired date cannot be in the past or present")));
    }


    @Test
    void createConductor_shouldFail_whenMedicalExpiryBeforeIssued() throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.now())
                .domedicalexpired(LocalDate.now().minusDays(1))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createConductor_shouldFail_whenMedicalValidityExceedsSixMonths() throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.now())
                .domedicalexpired(LocalDate.now().plusMonths(7))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",hasItem("Medical validity cannot exceed 6 months")));
    }

    @Test
    void createConductor_shouldSucceed_whenMedicalValidityExactlySixMonths() throws Exception {

        ConductorRequestDto dto = ConductorRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .domedicalissued(LocalDate.now())
                .domedicalexpired(LocalDate.now().plusMonths(6))
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }


}
