package lk.ashan.routenetlkserverapllication.module.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.DriverDtoFactory;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
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
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/drivers";

    @Test
    void createDriver_shouldSucceed_whenUniqueValid_DriverCreateRequest() throws Exception {

        DriverCreateRequestDto createRequestDto = DriverDtoFactory.createUniqueDriverCreateRequest();

      mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated());
    }

    //Mandatory Attributes

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createDriver_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<DriverCreateRequestDto> mutator) throws Exception {
        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();
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
                Arguments.of("number", "Number can not be empty", (Consumer<DriverCreateRequestDto>) dto -> dto.setNumber(null)),
                Arguments.of("licensenumber", "License number can not be empty", (Consumer<DriverCreateRequestDto>) dto -> dto.setLicensenumber(null)),
                Arguments.of("dolicenseissued", "License issued date is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setDolicenseissued(null)),
                Arguments.of("dolicenseexpired", "License expired date is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setDolicenseexpired(null)),
                Arguments.of("domedicalissued", "Medical issued date is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setDomedicalissued(null)),
                Arguments.of("domedicalexpired", "Medical expired date is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setDomedicalexpired(null)),
                Arguments.of("licensecategory", "License Category is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setLicensecategory(null)),
                Arguments.of("crewstatus", "Crew status is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setCrewstatus(null)),
                Arguments.of("routefamiliaritylevel", "Route Familiarity Level is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setRoutefamiliaritylevel(null)),
                Arguments.of("employee", "Employee is mandatory", (Consumer<DriverCreateRequestDto>) dto -> dto.setEmployee(null))
        );
    }

    // Driver Number - Invalid patterns
    @ParameterizedTest
    @ValueSource(strings = {
            "DRV1234-123",    // missing hyphen after DRV
            "DRV-123-123",    // only 3 digits instead of 4
            "DRV-12345-123",  // 5 digits instead of 4
            "DRV-1234-12",    // only 2 digits instead of 3
            "drv-1234-123",   // lowercase prefix
            "DRV-12A4-123",   // letter in first number block
            "DRV-1234-12B",   // letter in second number block
            " DRV-1234-123",  // leading space
            "DRV-1234-123 ",  // trailing space
            "DRV--1234-123",  // double hyphen
            "DRV-1234_123",   // underscore instead of hyphen
            "ABC-1234-123"    // wrong prefix
    })
    void createDriver_shouldFail_whenNumberFormatIsInvalid(String invalidNumber) throws Exception {
        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();
        dto.setNumber(invalidNumber);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid driver number"
                ));
    }

    // Driver Number - Valid patterns
    @ParameterizedTest
    @ValueSource(strings = {
            "DRV-0001-001",
            "DRV-1234-123",
            "DRV-9999-999"
    })
    void createDriver_shouldSucceed_whenNumberFormatIsValid(String validNumber) throws Exception {
        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();
        dto.setNumber(validNumber);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @CsvSource({
            //id, category, licenseNumber, isValid
            "1, B, B12345678901, true",
            "1, B, B1234567890, false",     // too short
            "1, B, C11234567890, false",    // wrong prefix
            "2, C1, C11234567894, true",
            "2, C1, C112345678, false",     // too short
            "2, C1, B12345678901, false"    // wrong prefix
    })
    void createDriver_shouldValidateLicenseNumberByCategory(Integer id, String name, String licenseNumber, boolean isValid) throws Exception {
        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();
        dto.setLicensecategory(DriverDtoFactory.licenseCategoryDto(id,name));
        dto.setLicensenumber(licenseNumber);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(isValid ? status().isCreated() : status().isBadRequest());
    }

    //Flow Validation
    @Test
    void createDriver_shouldFail_whenLicenseExpired() throws Exception {
        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();
        dto.setDolicenseexpired(LocalDate.now().minusDays(1)); // expired license

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "dolicenseexpired: License Expired date cannot be in the past or present"
                ));
    }

    @ParameterizedTest
    @MethodSource("pastOrPresentDateProvider")
    void createDriver_shouldValidatePastOrPresentDates(
            String field,
            LocalDate value,
            boolean isValid) throws Exception {

        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();

        switch (field) {
            case "dolicenseissued" -> dto.setDolicenseissued(value);
            case "domedicalissued" -> dto.setDomedicalissued(value);
        }

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(isValid ? status().isCreated() : status().isBadRequest());
    }

    static Stream<Arguments> pastOrPresentDateProvider() {
        return Stream.of(
                Arguments.of("dolicenseissued", LocalDate.now(), true),
                Arguments.of("dolicenseissued", LocalDate.now().plusDays(1), false),
                Arguments.of("domedicalissued", LocalDate.now(), true),
                Arguments.of("domedicalissued", LocalDate.now().plusDays(1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("futureDateProvider")
    void createDriver_shouldValidateFutureDates(
            String field,
            LocalDate value,
            boolean isValid) throws Exception {

        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();

        switch (field) {
            case "dolicenseexpired" -> dto.setDolicenseexpired(value);
            case "domedicalexpired" -> dto.setDomedicalexpired(value);
        }

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(isValid ? status().isCreated() : status().isBadRequest());
    }

    static Stream<Arguments> futureDateProvider() {
        return Stream.of(
                Arguments.of("dolicenseexpired", LocalDate.now().plusDays(1), true),
                Arguments.of("dolicenseexpired", LocalDate.now(), false),
                Arguments.of("domedicalexpired", LocalDate.now().plusDays(1), true),
                Arguments.of("domedicalexpired", LocalDate.now(), false)
        );
    }

    @Test
    void createDriver_shouldFail_whenLicenseNumberAlreadyExists() throws Exception {
        DriverCreateRequestDto createRequestDto = DriverDtoFactory.createUniqueDriverCreateRequest();
        createRequestDto.setLicensenumber("B12345678902");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "License number already exists"
                ));
    }

    @Test
    void createDriver_shouldFail_whenDriverNumberAlreadyExists() throws Exception {
        DriverCreateRequestDto dto = DriverDtoFactory.createUniqueDriverCreateRequest();
        dto.setNumber("DRV-2025-002");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Driver number already exists"
                ));
    }

    @ParameterizedTest
    @CsvSource({
            // id, name, isValid
            "1, Eligible, true",
            "2, Ineligible, false",
            "3, Active, false",
            "4, Inactive, false"
    })
    void createDriver_shouldValidateCrewStatus(int id, String name, boolean isValid) throws Exception {
        DriverCreateRequestDto createRequestDto = DriverDtoFactory.createUniqueDriverCreateRequest();
        createRequestDto.setCrewstatus(DriverDtoFactory.crewStatusDto(id,name));

        var request = mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestDto)));

        if (isValid) {
            request.andExpect(status().isCreated());
        } else {
            request.andExpect(status().isBadRequest())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "New driver must have status 'ELIGIBLE'"
                    ));
        }
    }

    @Test
    void createDriver_shouldFail_whenLicenseValidityPeriodIsExceed() throws  Exception{
        DriverCreateRequestDto createRequestDto = DriverDtoFactory.createUniqueDriverCreateRequest();
        createRequestDto.setDolicenseissued(LocalDate.of(2022,10,10));
        createRequestDto.setDolicenseexpired(LocalDate.of(2035,10,10));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid license validity period"
                ));
    }

    @Test
    void createDriver_shouldFail_whenMedicalValidityPeriodIsExceed() throws  Exception{
        DriverCreateRequestDto createRequestDto = DriverDtoFactory.createUniqueDriverCreateRequest();
        createRequestDto.setDomedicalissued(LocalDate.of(2021,10,10));
        createRequestDto.setDomedicalexpired(LocalDate.of(2035,10,10));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Medical validity cannot exceed 6 months"
                ));
    }

    //Update Tests
    @Test
    void updateDriver_shouldFail_whenLicenseIssuedDateInFuture() throws Exception {
        DriverUpdateRequestDto dto = DriverDtoFactory.createUniqueDriverUpdateRequest();
        dto.setDolicenseissued(LocalDate.now().plusDays(1));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDriver_shouldFail_whenLicenseValidityPeriodIsExceed() throws  Exception{
        DriverUpdateRequestDto updateRequestDto = DriverDtoFactory.createUniqueDriverUpdateRequest();
        updateRequestDto.setDolicenseissued(LocalDate.of(2025,12,12));
        updateRequestDto.setDolicenseexpired(LocalDate.of(2035,12,12));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid license validity period"
                ));
    }

    @Test
    void updateDriver_shouldFail_whenMedicalValidityPeriodIsExceed() throws Exception {
        DriverUpdateRequestDto dto = DriverDtoFactory.createUniqueDriverUpdateRequest();
        // issued and expiry more than 6 months apart, expiry after today
        dto.setDomedicalissued(LocalDate.of(2025, 6, 1));
        dto.setDomedicalexpired(LocalDate.of(2026, 1, 15));

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Medical validity cannot exceed 6 months"
                ));
    }

    @Test
    void updateDriver_shouldSucceed_whenUniquenessFieldsUnchanged() throws Exception {
        DriverUpdateRequestDto dto = DriverDtoFactory.createUniqueDriverUpdateRequest();

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
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
    void updateDriver_shouldValidateRouteFamiliarityLevelTransitions(
            String currentLevel,
            Integer id,
            String newLevel,
            boolean isValid
    ) throws Exception {
        DriverUpdateRequestDto dto = DriverDtoFactory.createUniqueDriverUpdateRequest();
        dto.setRoutefamiliaritylevel(DriverDtoFactory.routeFamiliarityLevelDto(id, newLevel));

        ResultActions result = mockMvc.perform(put(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        if (isValid) {
            result.andExpect(status().isCreated());
        } else {
            result.andExpect(status().isBadRequest())
                    .andExpect(ValidationResultMatcher.expectValidationError(
                            "Cannot skip route familiarity levels when upgrading from " + currentLevel
                    ));
        }
    }




}


