package lk.ashan.routenetlkserverapllication.module.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
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
class DriverControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/drivers";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createDriver_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<DriverCreateRequestDto> mutator) throws Exception {
      DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                //.number("DRV-2025-006")
                .licensenumber("B39345678905")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-06-27"))
                .domedicalexpired(LocalDate.parse("2025-12-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
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
//                Arguments.of("number", "Number can not be empty", (Consumer<DriverCreateRequestDto>) dto -> dto.setNumber(null)),
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
        DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(3).callingname("sunil").build())
                .licensenumber(licenseNumber)
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.now().minusDays(80))
                .domedicalexpired(LocalDate.now().plusDays(100))
                .licensecategory(LicenseCategoryDto.builder().id(id).name(name).build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(isValid ? status().isCreated() : status().isBadRequest());
    }

    @Test
    void createDriver_shouldFail_whenLicenseExpired() throws Exception {
        DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(3).callingname("sunil").build())
                .licensenumber("B39345678905")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2025-07-12"))
                .domedicalissued(LocalDate.now().minusDays(80))
                .domedicalexpired(LocalDate.now().plusDays(100))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString("dolicenseexpired: License Expired date cannot be in the past or present"))));
    }

    @ParameterizedTest
    @MethodSource("pastOrPresentDateProvider")
    void createDriver_shouldValidatePastOrPresentDates(
            String field,LocalDate value,boolean isValid) throws Exception {

        DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(2).callingname("Rohan").build())
                //.number("DRV-2025-006")
                .licensenumber("B39345678905")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-06-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        switch (field) {
            case "dolicenseissued" -> dto.setDolicenseissued(value);
            case "domedicalissued" -> dto.setDomedicalissued(value);
        }

        mockMvc.perform(post(API_URL)
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

    @Test
    void createDriver_shouldFail_whenDuplicateField() throws Exception {
        DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(2).callingname("Rohan").build())
                //.number("DRV-2025-006")
                .licensenumber("B12345678902")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-06-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect( jsonPath("$.details",
                        hasItem(containsString("License number already exists")))           );
    }

    @Test
    void createDriver_shouldFail_whenLicenseValidityPeriodIsExceeded() throws Exception {
        DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(2).callingname("Rohan").build())
                //.number("DRV-2025-006")
                .licensenumber("B39345678905")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-06-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        dto.setDolicenseissued(
                LocalDate.of(2022, 10, 10)
        );

        dto.setDolicenseexpired(
                LocalDate.of(2035, 10, 10)
        );

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.details",
                                hasItem(containsString("Invalid license validity period (Max 4 years")))
                );
    }

    @Test
    void createDriver_shouldFail_whenMedicalValidityPeriodIsExceeded() throws Exception {

        DriverCreateRequestDto dto = DriverCreateRequestDto.builder()
                .employee(EmployeeSummaryDto.builder().id(2).callingname("Rohan").build())
                .licensenumber("B39345678905")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2026-01-27"))
                .domedicalexpired(LocalDate.parse("2026-06-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        dto.setDomedicalissued(
                LocalDate.of(2021, 10, 10)
        );

        dto.setDomedicalexpired(
                LocalDate.of(2035, 10, 10)
        );

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.details",
                                hasItem(containsString("Medical validity cannot exceed 6 months")))
                );
    }

    @Test
    void updateDriver_shouldFail_whenLicenseNumberChanged() throws Exception {
        DriverUpdateRequestDto dto = DriverUpdateRequestDto.builder()
                .id(1)
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .licensenumber("B12345678902")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-12-27"))
                .domedicalexpired(LocalDate.parse("2026-05-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        dto.setLicensenumber("B12345678999");

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString("License number cannot be changed")))
                );
    }

    @Test
    void updateDriver_shouldFail_whenEmployeeChanged() throws Exception {

        DriverUpdateRequestDto dto = DriverUpdateRequestDto.builder()
                .id(1)
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .licensenumber("B12345678902")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-12-27"))
                .domedicalexpired(LocalDate.parse("2026-05-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B1").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(1).name("Low").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

                dto.setEmployee(EmployeeSummaryDto.builder().id(3).callingname("sunil").build());


        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details", hasItem(containsString("Employee cannot be reassigned"))));
    }

    @Test
    void updateDriver_shouldFail_whenLicenseIssuedDateChanged() throws Exception {

        DriverUpdateRequestDto dto = DriverUpdateRequestDto.builder()
                .id(1)
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .licensenumber("B12345678902")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-12-27"))
                .domedicalexpired(LocalDate.parse("2026-05-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(2).name("Medium").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        dto.setDolicenseissued(LocalDate.now().minusYears(1));

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details", hasItem(containsString("License issued date cannot be modified"))));

    }

    @ParameterizedTest
    @CsvSource({
            "1, Eligible, true",
            "2, Ineligible, true",
            "3, Active, true",
            "4, Inactive, true"
    })
    void updateDriver_shouldValidateCrewStatusTransitions(
            int id,
            String name,
            boolean isValid
    ) throws Exception {

        DriverUpdateRequestDto dto = DriverUpdateRequestDto.builder()
                .id(1)
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .licensenumber("B12345678902")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-12-27"))
                .domedicalexpired(LocalDate.parse("2026-05-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(2).name("Medium").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();

        dto.setCrewstatus(
                CrewStatusDto.builder().id(id).name(name).build()
        );

        var result =
                mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)));

        if (isValid) {

            result.andExpect(status().isOk());

        } else {

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details",
                            hasItem(containsString("License number cannot be changed")))
                    );
        }

    }


    @ParameterizedTest
    @CsvSource({
            "1, B, true",
            "2, C1, false",
            "3, E, false"
    })
    void updateDriver_shouldValidateLicenseCategoryChange(
            int id,
            String name,
            boolean isValid
    ) throws Exception {

        DriverUpdateRequestDto dto = DriverUpdateRequestDto.builder()
                .id(1)
                .employee(EmployeeSummaryDto.builder().id(1).callingname("sunil").build())
                .licensenumber("B12345678902")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-12-27"))
                .domedicalexpired(LocalDate.parse("2026-05-27"))
                .licensecategory(LicenseCategoryDto.builder().id(1).name("B").build())
                .routefamiliaritylevel(RouteFamiliarityLevelDto.builder().id(2).name("Medium").build())
                .crewstatus(CrewStatusDto.builder().id(1).name("Eligible").build())
                .build();
        dto.setLicensecategory(
                LicenseCategoryDto.builder().id(id).name(name).build()
        );

        var result =
                mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)));

        if (isValid) {
            result.andExpect(status().isOk());
        } else {
            result.andExpect(status().isBadRequest());
        }

    }
}
