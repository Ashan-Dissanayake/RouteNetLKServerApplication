package lk.ashan.routenetlkserverapllication.module.trip.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.DtoFactory;
import lk.ashan.routenetlkserverapllication.config.factory.TripDtoFactory;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.RouteSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.*;
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

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = "spring.sql.init.mode=never")
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/trips";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createTrip_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<TripCreateRequestDto> mutator) throws Exception {
        TripCreateRequestDto dto = TripDtoFactory.createUniqueTripRequestDto();
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
                Arguments.of("branch", "Branch is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setBranch(null)),
                Arguments.of("triptype", "Trip type is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setTriptype(null)),
                Arguments.of("permite", "Permit is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setPermite(null)),
                Arguments.of("doservice", "Service date is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setDoservice(null)),
                Arguments.of("todepature", "Departure time is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setTodepature(null)),
                Arguments.of("toarrival", "Arrival time is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setToarrival(null)),
                //Arguments.of("notrip", "Number is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setNotrip(null)),
                //Arguments.of("tripstatus", "Trip status is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setTripstatus(null)),
                Arguments.of("originterminal", "Origin terminal is mandatory", (Consumer<TripCreateRequestDto>) dto -> dto.setOriginterminal(null))
        );
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createTrip_shouldSucceed_whenPermitValidAndVehicleAvailable() throws Exception {
        // Given: Valid permit with available vehicle
        TripCreateRequestDto dto = TripCreateRequestDto.builder()
                .branch(DtoFactory.branchSummaryResponseDto(1, "Colombo Head Office"))
                .triptype(TripDtoFactory.tripTypeDto(1, "Daily"))
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(1)
                                .number("2696")
                                .route(RouteSummaryRequestDto.builder().id(1).number("4-7").build())
                                .build()
                )
                .doservice(LocalDate.parse("2026-02-17"))
                .todepature(LocalTime.parse("08:00:00"))
                .toarrival(LocalTime.parse("10:00:00"))
                //.tripstatus(TripDtoFactory.tripStatusDto(1, "Planned"))
                .originterminal(TripDtoFactory.originTerminalDto(1, "Terminal A"))
                .build();

        // When: Creating the trip
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Trip created successfully with READY status
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.tripstatus.name").value("Planned"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createTrip_shouldCreateWithOverrideStatus_whenVehicleUnderMaintenance() throws Exception {
        // Given: Permit with vehicle in MAINTENANCE status
        TripCreateRequestDto dto = TripCreateRequestDto.builder()
                .branch(DtoFactory.branchSummaryResponseDto(1, "Main Depot"))
                .triptype(TripDtoFactory.tripTypeDto(1, "Daily"))
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(2)
                                .number("2697")
                                .route(RouteSummaryRequestDto.builder().id(1).number("4-7").build())
                                .build()
                )
                .doservice(LocalDate.parse("2026-02-17"))
                .todepature(LocalTime.parse("09:00:00"))
                .toarrival(LocalTime.parse("11:00:00"))
                .originterminal(TripDtoFactory.originTerminalDto(1, "pettah"))
                .build();

        // When: Creating the trip
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Trip created with NEEDS_VEHICLE_OVERRIDE status
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.tripstatus.name").value("Need vehicle override"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void triggerSolver_shouldReturnSuggestion_whenVehicleUnderMaintenance() throws Exception {

        // Given: Trip in NEEDS_VEHICLE_OVERRIDE status
        Integer tripId = 2; // Assume trip already created in NEEDS_VEHICLE_OVERRIDE status

        // When: Triggering solver
        mockMvc.perform(post(apiUrl + "/{tripId}/override/suggest", tripId))
                // Then: Solver returns vehicle suggestion
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tripId").value(tripId))
                .andExpect(jsonPath("$.data.suggestedVehicleId").exists());
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createTrip_shouldFail_whenMinimumGapViolated() throws Exception {

        // When: Creating new trip at 08:15 (only 15 minutes gap - violates 30 min requirement)
        TripCreateRequestDto violatingTrip = TripCreateRequestDto.builder()
                .branch(DtoFactory.branchSummaryResponseDto(1, "Colombo head office"))
                .triptype(TripDtoFactory.tripTypeDto(2, "Weekday"))
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(1)  // Permit 2696, Route 4-7
                                .number("2696")
                                .route(RouteSummaryRequestDto.builder()
                                        .id(1)
                                        .number("4-7")
                                        .build())
                                .build()
                )
                .doservice(LocalDate.parse("2026-02-16"))  // Same date as Trip 1
                .todepature(LocalTime.parse("08:15:00"))   // 15 min after Trip 1 (violates 30 min gap)
                .toarrival(LocalTime.parse("10:15:00"))
                .originterminal(TripDtoFactory.originTerminalDto(1, "pettah"))  // Same terminal
                .build();

        // Then: Request rejected with conflict error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(violatingTrip)))
                .andExpect(status().isConflict())  // 409 CONFLICT
                .andExpect(jsonPath("$.type").value("https://localhost/errors/business-rule-violation"))
                .andExpect(jsonPath("$.title").value("Business rule violation"))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.code").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.details[0]").value(containsString("another trip departs from the same terminal within 30 minutes")))
                .andExpect(jsonPath("$.instance").value("http://localhost/trips"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void backward_createTrip_shouldFail_whenMinimumGapViolated() throws Exception {

        // Given: Existing trip at 08:00
        // When: Creating trip at 07:45 (15 minutes BEFORE existing trip)
        TripCreateRequestDto violatingTrip = TripCreateRequestDto.builder()
                .branch(DtoFactory.branchSummaryResponseDto(1, "Colombo head office"))
                .triptype(TripDtoFactory.tripTypeDto(2, "Weekday"))
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(1)
                                .number("2696")
                                .route(RouteSummaryRequestDto.builder().id(1).number("4-7").build())
                                .build()
                )
                .doservice(LocalDate.parse("2026-02-16"))
                .todepature(LocalTime.parse("07:45:00"))  // 15 min BEFORE Trip 1
                .toarrival(LocalTime.parse("09:45:00"))
                .originterminal(TripDtoFactory.originTerminalDto(1, "pettah"))
                .build();

        // Then: Rejected (gap works both directions)
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(violatingTrip)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details[0]").value(containsString("30 minutes")));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateTrip_shouldFail_whenUpdatedTimeBreaksGap() throws Exception {

        Integer tripIdToUpdate = 3;  // Trip 3 from data.sql

        // When: Updating Trip 3 departure time to 08:20 (breaks 30-min gap with Trip 1 at 08:00)
        TripUpdateRequestDto updateDto = TripUpdateRequestDto.builder()
                .id(tripIdToUpdate)
                .branch(DtoFactory.branchSummaryResponseDto(1, "Colombo head office"))
                .triptype(TripDtoFactory.tripTypeDto(2, "Weekday"))
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(1)  // Same permit as Trip 1
                                .number("2696")
                                .route(RouteSummaryRequestDto.builder()
                                        .id(1)
                                        .number("4-7")
                                        .build())
                                .build()
                )
                .notrip(2)
                .doservice(LocalDate.parse("2026-02-16"))  // Same date as Trip 1
                .todepature(LocalTime.parse("08:20:00"))   // 20 min after Trip 1 (violates 30 min gap)
                .toarrival(LocalTime.parse("10:20:00"))
                .originterminal(TripDtoFactory.originTerminalDto(1, "pettah"))  // Same terminal
                .build();

        // Then: Update rejected with conflict error
        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isConflict())  // 409 CONFLICT
                .andExpect(jsonPath("$.type").value("https://localhost/errors/business-rule-violation"))
                .andExpect(jsonPath("$.title").value("Business rule violation"))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.code").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.details[0]", anyOf(
                        containsString("gap violation"),
                        containsString("gap rule")
                )))
                .andExpect(jsonPath("$.instance").value("http://localhost/trips"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void completeTrip_shouldSucceed_whenTripInProgress() throws Exception {

        Integer tripId = 4;

        // When: Completing trip
        mockMvc.perform(post(apiUrl + "/{tripId}/complete-trip", tripId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(tripId))
                .andExpect(jsonPath("$.data.tripstatus.name").value("Completed"))
                .andExpect(jsonPath("$.meta.action").value("trip_completed"))
                .andExpect(jsonPath("$.meta.status").value("COMPLETED"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void completeTrip_withActualArrival_shouldSucceedAndUpdateTime() throws Exception {

        // Given: Trip 4 is IN_PROGRESS
        Integer tripId = 4;
        String actualTime = "16:30:00";  // Original: 16:00, Actual: 16:30 (delayed)

        // When: Completing trip with actual arrival time
        mockMvc.perform(post(apiUrl + "/{tripId}/complete-trip", tripId)
                        .param("actualTime", actualTime))
                // Then: Completed with updated arrival
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(tripId))
                .andExpect(jsonPath("$.data.tripstatus.name").value("Completed"))
                .andExpect(jsonPath("$.data.toarrival").value(actualTime))
                .andExpect(jsonPath("$.meta.action").value("trip_completed"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateTrip_shouldFail_whenTripCompleted() throws Exception {

        // Given: Trip 5 is COMPLETED (from data.sql)
        // Trip 5: 2026-02-14, 06:00-08:00, Status = Completed (id 7)
        Integer completedTripId = 5;

        TripUpdateRequestDto updateDto = TripUpdateRequestDto.builder()
                .id(completedTripId)
                .branch(DtoFactory.branchSummaryResponseDto(1, "Colombo head office"))
                .triptype(TripDtoFactory.tripTypeDto(2, "Weekday"))
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(1)
                                .number("2696")
                                .route(RouteSummaryRequestDto.builder()
                                        .id(1)
                                        .number("4-7")
                                        .build())
                                .build()
                )
                .doservice(LocalDate.parse("2026-02-16"))
                .todepature(LocalTime.parse("07:00:00"))  // Try to change time
                .toarrival(LocalTime.parse("09:00:00"))
                .notrip(4)  // Original trip number
                .originterminal(TripDtoFactory.originTerminalDto(1, "pettah"))
                .build();

        // When: Attempting to update
        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                // Then: Update blocked
                .andExpect(status().isConflict())  // 409 CONFLICT
                .andExpect(jsonPath("$.type").value("https://localhost/errors/business-rule-violation"))
                .andExpect(jsonPath("$.title").value("Business rule violation"))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.code").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.details[0]").value(containsString("Closed trips cannot be edited")))
                .andExpect(jsonPath("$.instance").value("http://localhost/trips"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void completeTrip_shouldFail_whenAlreadyCompleted() throws Exception {

        // Given: Trip 5 is already COMPLETED
        Integer completedTripId = 5;

        // When: Attempting to complete again
        mockMvc.perform(post(apiUrl + "/{tripId}/complete-trip", completedTripId))
                // Then: Request rejected
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details[0]").value(containsString("Only IN_PROGRESS trips can be completed. Current status: Completed")));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void completeTrip_shouldFail_whenNotInProgress() throws Exception {

        // Given: Trip 1 is READY (not IN_PROGRESS)
        Integer readyTripId = 1;

        // When: Attempting to complete
        mockMvc.perform(post(apiUrl + "/{tripId}/complete-trip", readyTripId))
                // Then: Request rejected
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details[0]").value(containsString("Only IN_PROGRESS trips can be completed. Current status: Ready")));
    }


    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void cancelTrip_shouldSucceed_whenTripIsReady() throws Exception {
        // Given: Trip in READY status
        Integer tripId = 1;

        // When: Cancelling trip
        mockMvc.perform(post(apiUrl + "/{tripId}/cancel-trip", tripId))
                // Then: Status changes to CANCELLED, overrides removed
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId))
                .andExpect(jsonPath("$.tripstatus.name").value("CANCELLED"));
    }


    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void cancelTrip_shouldFail_whenTripCompleted() throws Exception {
        // Given: Trip in COMPLETED status
        Integer tripId = 5;

        // When: Attempting to cancel
        mockMvc.perform(post(apiUrl + "/{tripId}/cancel-trip", tripId))
                // Then: Cancellation blocked
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details[0]", containsString("Cannot cancel a completed trip")));
    }


    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createTrip_shouldSucceed_whenCrossingMidnight() throws Exception {
        // Given: Trip departing 23:55, arriving 00:05 next day
        // Using existing data from data.sql:
        // - Branch 1 (Colombo head office)
        // - Permit 1 (route 4-7, mingap 30 minutes)
        // - Origin terminal 1 (pettah)
        // - Trip type 2 (Weekday)
        // - Trip status 1 (Planned)

        TripCreateRequestDto dto = TripCreateRequestDto.builder()
                .branch(BranchSummaryResponseDto.builder()
                        .id(1)
                        .name("Colombo head office")
                        .build())
                .triptype(TripTypeDto.builder()
                        .id(2)
                        .name("Weekday")
                        .build())
                .permite(PermitSummaryRequestDto.builder()
                        .id(1)
                        .route(RouteSummaryRequestDto.builder()
                                .id(1)
                                .mingapminutes(30)
                                .build())
                        .build())
                .doservice(LocalDate.parse("2026-02-17")) // Different date from existing trips
                .todepature(LocalTime.parse("23:55:00"))
                .toarrival(LocalTime.parse("00:05:00")) // Next day - crosses midnight
                .notrip(30)
                .tripstatus(TripStatusDto.builder()
                        .id(1)
                        .name("Planned")
                        .build())
                .originterminal(OriginTerminalDto.builder()
                        .id(1)
                        .name("pettah")
                        .build())
                .build();

        // When: Creating midnight boundary trip
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Trip created successfully
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.todepature").value("23:55:00"))
                .andExpect(jsonPath("$.data.toarrival").value("00:05:00"));


    }

}
