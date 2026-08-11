package lk.ashan.routenetlkserverapllication.module.crew.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.DriverService;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverController.class)
@Import(TestSecurityConfiguration.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverService driverService;

    private static final String API_URL = "/drivers";

    // =========================================================
    // GET /drivers
    // =========================================================

    @Test
    void getDrivers_shouldReturn200_whenAuthorized() throws Exception {

        DriverDetailResponseDto response = DriverDetailResponseDto.builder()
                .id(1)
                .number("DRV001")
                .licensenumber("B1234567")
                .build();

        when(driverService.getDrivers())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-view")))
                .andExpect(status().isOk());

        verify(driverService).getDrivers();
    }

    @Test
    void getDrivers_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(driverService);
    }

    @Test
    void getDrivers_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(driverService);
    }

    @Test
    void searchDrivers_shouldCallSearchService_whenQueryParamsProvided()
            throws Exception {

        DriverDetailResponseDto response = DriverDetailResponseDto.builder()
                .id(1)
                .number("DRV001")
                .licensenumber("B1234567")
                .build();

        when(driverService.searchDriver(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("licensenumber", "B1234567")
                        .with(user("test-user")
                                .authorities(() -> "driver-view")))
                .andExpect(status().isOk());

        verify(driverService).searchDriver(any());
        verify(driverService, never()).getDrivers();
    }

    // =========================================================
    // POST /drivers
    // =========================================================

    @Test
    void createDriver_shouldReturn201_whenRequestIsValid() throws Exception {

        DriverCreateRequestDto dto = validCreateRequest();

        DriverDetailResponseDto response = DriverDetailResponseDto.builder()
                .id(1)
                .number("DRV001")
                .licensenumber("B1234567")
                .build();

        when(driverService.createDriver(any(DriverCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(driverService)
                .createDriver(any(DriverCreateRequestDto.class));
    }

    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createDriver_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<DriverCreateRequestDto> mutator
    ) throws Exception {

        DriverCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(driverService);
    }

    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "licensenumber",
                        "License number can not be empty",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setLicensenumber(null)
                ),

                Arguments.of(
                        "dolicenseissued",
                        "License issued date is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDolicenseissued(null)
                ),

                Arguments.of(
                        "dolicenseexpired",
                        "License expired date is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDolicenseexpired(null)
                ),

                Arguments.of(
                        "domedicalissued",
                        "Medical issued date is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDomedicalissued(null)
                ),

                Arguments.of(
                        "domedicalexpired",
                        "Medical expired date is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDomedicalexpired(null)
                ),

                Arguments.of(
                        "licensecategory",
                        "License Category is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setLicensecategory(null)
                ),

                Arguments.of(
                        "crewstatus",
                        "Crew status is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setCrewstatus(null)
                ),

                Arguments.of(
                        "routefamiliaritylevel",
                        "Route Familiarity Level is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setRoutefamiliaritylevel(null)
                ),

                Arguments.of(
                        "employee",
                        "Employee is mandatory",
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setEmployee(null)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidLicenseNumberProvider")
    void createDriver_shouldReturn400_whenLicenseNumberIsInvalid(
            String invalidLicenseNumber
    ) throws Exception {

        DriverCreateRequestDto dto = validCreateRequest();

        dto.setLicensenumber(invalidLicenseNumber);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString("Invalid License Number"))));

        verifyNoInteractions(driverService);
    }

    static Stream<String> invalidLicenseNumberProvider() {

        return Stream.of(
                "1234567",
                "C1234567",
                "B123456",
                "B12345678",
                "B123456A",
                "BB123456",
                "B123 456"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCreateDateProvider")
    void createDriver_shouldReturn400_whenDateValidationFails(
            Consumer<DriverCreateRequestDto> mutator,
            String errorMessage
    ) throws Exception {

        DriverCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(errorMessage))));

        verifyNoInteractions(driverService);
    }

    static Stream<Arguments> invalidCreateDateProvider() {

        return Stream.of(

                Arguments.of(
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDolicenseissued(
                                        LocalDate.now().plusDays(1)
                                ),
                        "License issued date is cannot be in the future"
                ),

                Arguments.of(
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDolicenseexpired(
                                        LocalDate.now()
                                ),
                        "License Expired date cannot be in the past or present"
                ),

                Arguments.of(
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDolicenseexpired(
                                        LocalDate.now().minusDays(1)
                                ),
                        "License Expired date cannot be in the past or present"
                ),

                Arguments.of(
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDomedicalissued(
                                        LocalDate.now().plusDays(1)
                                ),
                        "Medical issued date is cannot be in the future"
                ),

                Arguments.of(
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDomedicalexpired(
                                        LocalDate.now()
                                ),
                        "Medical expired date cannot be in the past or present"
                ),

                Arguments.of(
                        (Consumer<DriverCreateRequestDto>)
                                dto -> dto.setDomedicalexpired(
                                        LocalDate.now().minusDays(1)
                                ),
                        "Medical expired date cannot be in the past or present"
                )
        );
    }

    @Test
    void createDriver_shouldReturn403_whenWrongAuthority() throws Exception {

        DriverCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(driverService);
    }

    @Test
    void createDriver_shouldReturn401_whenAnonymous() throws Exception {

        DriverCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(driverService);
    }

    // =========================================================
    // PUT /drivers
    // =========================================================

    @Test
    void updateDriver_shouldReturn200_whenRequestIsValid() throws Exception {

        DriverUpdateRequestDto dto = validUpdateRequest();

        DriverDetailResponseDto response = DriverDetailResponseDto.builder()
                .id(1)
                .number("DRV001")
                .licensenumber("B1234567")
                .build();

        when(driverService.updateDriver(any(DriverUpdateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(driverService)
                .updateDriver(any(DriverUpdateRequestDto.class));
    }

    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updateDriver_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<DriverUpdateRequestDto> mutator
    ) throws Exception {

        DriverUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(driverService);
    }

    static Stream<Arguments> missingUpdateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "id",
                        "Id is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setId(null)
                ),

                Arguments.of(
                        "licensenumber",
                        "License number can not be empty",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setLicensenumber(null)
                ),

                Arguments.of(
                        "dolicenseissued",
                        "License issued date is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDolicenseissued(null)
                ),

                Arguments.of(
                        "dolicenseexpired",
                        "License expired date is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDolicenseexpired(null)
                ),

                Arguments.of(
                        "domedicalissued",
                        "Medical issued date is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDomedicalissued(null)
                ),

                Arguments.of(
                        "domedicalexpired",
                        "Medical expired date is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDomedicalexpired(null)
                ),

                Arguments.of(
                        "licensecategory",
                        "License Category is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setLicensecategory(null)
                ),

                Arguments.of(
                        "crewstatus",
                        "Crew status is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setCrewstatus(null)
                ),

                Arguments.of(
                        "routefamiliaritylevel",
                        "Route Familiarity Level is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setRoutefamiliaritylevel(null)
                ),

                Arguments.of(
                        "employee",
                        "Employee is mandatory",
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setEmployee(null)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateLicenseNumberProvider")
    void updateDriver_shouldReturn400_whenLicenseNumberIsInvalid(
            String invalidLicenseNumber
    ) throws Exception {

        DriverUpdateRequestDto dto = validUpdateRequest();

        dto.setLicensenumber(invalidLicenseNumber);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString("Invalid License Number"))));

        verifyNoInteractions(driverService);
    }

    static Stream<String> invalidUpdateLicenseNumberProvider() {

        return Stream.of(
                "1234567",
                "C1234567",
                "B123456",
                "B12345678",
                "B123456A",
                "BB123456",
                "B123 456"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateDateProvider")
    void updateDriver_shouldReturn400_whenDateValidationFails(
            Consumer<DriverUpdateRequestDto> mutator,
            String errorMessage
    ) throws Exception {

        DriverUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(errorMessage))));

        verifyNoInteractions(driverService);
    }

    static Stream<Arguments> invalidUpdateDateProvider() {

        return Stream.of(

                Arguments.of(
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDolicenseissued(
                                        LocalDate.now().plusDays(1)
                                ),
                        "License issued date is cannot be in the future"
                ),

                Arguments.of(
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDolicenseexpired(
                                        LocalDate.now()
                                ),
                        "License Expired date cannot be in the past or present"
                ),

                Arguments.of(
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDolicenseexpired(
                                        LocalDate.now().minusDays(1)
                                ),
                        "License Expired date cannot be in the past or present"
                ),

                Arguments.of(
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDomedicalissued(
                                        LocalDate.now().plusDays(1)
                                ),
                        "Medical issued date is cannot be in the future"
                ),

                Arguments.of(
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDomedicalexpired(
                                        LocalDate.now()
                                ),
                        "Medical expired date cannot be in the past or present"
                ),

                Arguments.of(
                        (Consumer<DriverUpdateRequestDto>)
                                dto -> dto.setDomedicalexpired(
                                        LocalDate.now().minusDays(1)
                                ),
                        "Medical expired date cannot be in the past or present"
                )
        );
    }

    @Test
    void updateDriver_shouldReturn403_whenWrongAuthority() throws Exception {

        DriverUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "driver-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(driverService);
    }

    @Test
    void updateDriver_shouldReturn401_whenAnonymous() throws Exception {

        DriverUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(driverService);
    }

    // =========================================================
    // Test Data
    // =========================================================

    private DriverCreateRequestDto validCreateRequest() {

        return DriverCreateRequestDto.builder()
                .licensenumber("B1234567")
                .dolicenseissued(LocalDate.now().minusYears(1))
                .dolicenseexpired(LocalDate.now().plusYears(2))
                .domedicalissued(LocalDate.now().minusMonths(2))
                .domedicalexpired(LocalDate.now().plusMonths(4))
                .licensecategory(
                        LicenseCategoryDto.builder()
                                .id(1)
                                .name("B")
                                .build()
                )
                .crewstatus(
                        CrewStatusDto.builder()
                                .id(1)
                                .name("Eligible")
                                .build()
                )
                .routefamiliaritylevel(
                        RouteFamiliarityLevelDto.builder()
                                .id(1)
                                .name("Low")
                                .build()
                )
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .callingname("Sunil")
                                .build()
                )
                .build();
    }

    private DriverUpdateRequestDto validUpdateRequest() {

        return DriverUpdateRequestDto.builder()
                .id(1)
                .licensenumber("B1234567")
                .dolicenseissued(LocalDate.now().minusYears(1))
                .dolicenseexpired(LocalDate.now().plusYears(2))
                .domedicalissued(LocalDate.now().minusMonths(2))
                .domedicalexpired(LocalDate.now().plusMonths(4))
                .licensecategory(
                        LicenseCategoryDto.builder()
                                .id(1)
                                .name("B")
                                .build()
                )
                .crewstatus(
                        CrewStatusDto.builder()
                                .id(1)
                                .name("Eligible")
                                .build()
                )
                .routefamiliaritylevel(
                        RouteFamiliarityLevelDto.builder()
                                .id(1)
                                .name("Low")
                                .build()
                )
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .callingname("Sunil")
                                .build()
                )
                .build();
    }
}
