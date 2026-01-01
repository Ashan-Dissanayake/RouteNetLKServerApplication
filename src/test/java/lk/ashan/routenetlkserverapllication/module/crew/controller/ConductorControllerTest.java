package lk.ashan.routenetlkserverapllication.module.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.ConductorDtoFactory;
import lk.ashan.routenetlkserverapllication.config.factory.DriverDtoFactory;
import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ConductorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/conductors";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createConductor_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<ConductorCreateRequestDto> mutator) throws Exception {
        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
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
                Arguments.of("number", "Number can not be empty", (Consumer<ConductorCreateRequestDto>) dto -> dto.setNumber(null)),
                Arguments.of("domedicalissued", "Medical issued date is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setDomedicalissued(null)),
                Arguments.of("domedicalexpired", "Medical expired date is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setDomedicalexpired(null)),
                Arguments.of("crewstatus", "Crew status is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setCrewstatus(null)),
                Arguments.of("routefamiliaritylevel", "Route Familiarity Level is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setRoutefamiliaritylevel(null)),
                Arguments.of("employee", "Employee is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setEmployee(null))
        );
    }

    // Conductor Number - Invalid patterns
    @ParameterizedTest
    @ValueSource(strings = {
            "CON1234-123",    // missing hyphen after CON
            "CON-123-123",    // only 3 digits instead of 4
            "CON-12345-123",  // 5 digits instead of 4
            "CON-1234-12",    // only 2 digits instead of 3
            "con-1234-123",   // lowercase prefix
            "CON-12A4-123",   // letter in first number block
            "CON-1234-12B",   // letter in second number block
            " CON-1234-123",  // leading space
            "CON-1234-123 ",  // trailing space
            "CON--1234-123",  // double hyphen
            "CON-1234_123",   // underscore instead of hyphen
            "ABC-1234-123"    // wrong prefix
    })
    void createConductor_shouldFail_whenNumberFormatIsInvalid(String invalidNumber) throws Exception {
        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
        dto.setNumber(invalidNumber);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid conductor number"
                ));
    }

    @ParameterizedTest
    @MethodSource("pastOrPresentDateProvider")
    void createConductor_shouldValidatePastOrPresentDates(
            LocalDate value,
            boolean isValid) throws Exception {

        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
        dto.setDomedicalissued(value);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(isValid ? status().isCreated() : status().isBadRequest());
    }

    static Stream<Arguments> pastOrPresentDateProvider() {
        return Stream.of(
                Arguments.of(LocalDate.now(), true),
                Arguments.of( LocalDate.now().plusDays(1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("futureDateProvider")
    void createConductor_shouldValidateFutureDates(
            LocalDate value,
            boolean isValid) throws Exception {

        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
        dto.setDomedicalexpired(value);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(isValid ? status().isCreated() : status().isBadRequest());
    }

    static Stream<Arguments> futureDateProvider() {
        return Stream.of(
                Arguments.of( LocalDate.now().plusDays(1), true),
                Arguments.of( LocalDate.now(), false)
        );
    }

    @Test
    void createConductor_shouldFail_whenConductorNumberAlreadyExists() throws Exception {
        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
        dto.setNumber("CON-2025-002");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Conductor number already exists"
                ));
    }

    @Test
    void createConductor_shouldFail_whenMedicalIssueExpiryDateRangeMoreThanSix() throws Exception{
        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
        dto.setDomedicalissued(LocalDate.parse("2025-12-25"));
        dto.setDomedicalexpired(LocalDate.parse("2026-12-25"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Medical validity cannot exceed 6 months"
                ));
    }

    @Test
    void updateConductor_shouldFail_whenMedicalIssuedInFuture() throws Exception {
        ConductorUpdateRequestDto dto = ConductorDtoFactory.createUniqueConductorUpdateRequest();
        dto.setDomedicalissued(LocalDate.now().plusDays(1));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "domedicalissued: Medical issued date is cannot be in the future"
                ));
    }

    @ParameterizedTest
    @CsvSource({
            // current, id, new, isValid
            "Low, 1, Low, true",       // no change
            "Low, 2, Medium, true",    // valid upgrade
            "Low, 3, High, false",     // skip upgrade
            "Medium, 2, Medium, true", // no change
            "Medium, 3, High, true",   // valid upgrade
            "Medium, 1, Low, true",    // downgrade allowed
            "High, 3, High, true",     // no change
            "High, 2, Medium, true",   // downgrade allowed
            "High, 1, Low, true"       // downgrade allowed
    })
    void updateConductor_shouldValidateRouteFamiliarityLevelTransitions(
            String currentLevel,
            Integer newId,
            String newLevel,
            boolean isValid
    ) throws Exception {
        ConductorUpdateRequestDto dto = ConductorDtoFactory.createUniqueConductorUpdateRequest();
        dto.setRoutefamiliaritylevel(ConductorDtoFactory.routeFamiliarityLevelDto(newId, newLevel));

        ResultActions result = mockMvc.perform(put(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        if (isValid) {
            result.andExpect(status().isCreated());
        } else {
            result.andExpect(status().isConflict())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "Invalid route familiarity transition from " + currentLevel + " to " + newLevel
                    ));
        }
    }



}
