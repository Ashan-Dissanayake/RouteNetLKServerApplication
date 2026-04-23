package lk.ashan.routenetlkserverapllication.module.trip.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.RouteSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.*;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
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
@Sql(scripts = "/modules/trip/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/trip/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TripControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/trips";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createTrip_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<TripCreateRequestDto> mutator) throws Exception {
        TripCreateRequestDto dto = createBaseValidDto();
        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem(field + ": " + errorMessage)));
    }

    @Test
    void createTrip_shouldSucceed_whenPermitValidAndVehicleAvailable() throws Exception {
        TripCreateRequestDto dto = createBaseValidDto();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.tripstatus.name").value("Planned"));
    }

    @Test
    void createTrip_shouldCreateWithOverrideStatus_whenVehicleUnderMaintenance() throws Exception {
        TripCreateRequestDto dto = createBaseValidDto();
        dto.setPermite(
                PermitSummaryRequestDto.builder()
                        .id(4) // The ID of the 'OVERRIDE-TEST' permit
                        .number("1031")
                        .route(RouteSummaryRequestDto.builder().id(1).number("4-7").build())
                        .build()
        );

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tripstatus.name").value("Need vehicle override"));
    }

    @Test
    void triggerSolver_shouldReturnSuggestion_whenVehicleUnderMaintenance() throws Exception {
        Integer tripId = 3;

        mockMvc.perform(post(API_URL + "/{tripId}/override/suggest", tripId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tripId").value(tripId))
                .andExpect(jsonPath("$.data.suggestedVehicleId").exists());
    }

    @Test
    void createTrip_shouldFail_whenMinimumGapViolated() throws Exception {
        TripCreateRequestDto violatingDto = createBaseValidDto();
        violatingDto.setPermite(
                PermitSummaryRequestDto.builder()
                        .id(1)
                        .route(RouteSummaryRequestDto.builder().id(1).build())
                        .build()
        );

        violatingDto.setTodepature(LocalTime.parse("08:15:00"));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(violatingDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.details[0]", containsString("30 minutes")));
    }

    @Test
    void updateTrip_shouldFail_whenTripCompleted() throws Exception {
        Integer completedTripId = 6;

        TripUpdateRequestDto updateDto = TripUpdateRequestDto.builder()
                .id(completedTripId)
                .branch(BranchSummaryDto.builder().id(1).build())
                .triptype(TripTypeDto.builder().id(2).build())
                .permite(PermitSummaryRequestDto.builder().id(1).build())
                .todepature(LocalTime.parse("06:00:00"))
                .toarrival(LocalTime.parse("08:00:00"))
                .originterminal(OriginTerminalDto.builder().id(1).build())
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details[0]", containsString("Closed trips cannot be edited")));
    }

    @Test
    void completeTrip_withActualArrival_shouldSucceedAndUpdateTime() throws Exception {
        Integer tripId = 4;
        String actualTime = "16:30:00";

        mockMvc.perform(post(API_URL + "/{tripId}/complete-trip", tripId)
                        .param("actualTime", actualTime))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tripstatus.name").value("Completed"))
                .andExpect(jsonPath("$.data.toarrival").value(actualTime));
    }

    @Test
    void cancelTrip_shouldSucceed_whenTripIsReady() throws Exception {
        Integer tripId = 1;

        mockMvc.perform(post(API_URL + "/{tripId}/cancel-trip", tripId))
                .andExpect(status().isOk())
                // Added .data and changed to "Cancelled"
                .andExpect(jsonPath("$.data.tripstatus.name").value("Cancelled"));
    }

    // Helper method to keep tests clean
    private TripCreateRequestDto createBaseValidDto() {
        return TripCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .triptype(TripTypeDto.builder().id(1).name("Daily").build())
                .permite(PermitSummaryRequestDto.builder().id(1).number("ANG-NA7845-103-3").build())
                .todepature(LocalTime.parse("08:50:00"))
                .toarrival(LocalTime.parse("09:30:00"))
                .tripstatus(TripStatusDto.builder().id(1).name("Planned").build())
                .originterminal(OriginTerminalDto.builder().id(1).name("Pettah").build())
                .build();
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(
                Arguments.of("branch", "Branch is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setBranch(null)),
                Arguments.of("triptype", "Trip type is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setTriptype(null)),
                Arguments.of("permite", "Permit is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setPermite(null)),
                Arguments.of("todepature", "Departure time is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setTodepature(null)),
                Arguments.of("toarrival", "Arrival time is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setToarrival(null)),
                Arguments.of("originterminal", "Origin terminal is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setOriginterminal(null))
        );
    }
}
