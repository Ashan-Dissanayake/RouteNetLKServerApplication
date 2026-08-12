package lk.ashan.routenetlkserverapllication.module.permit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitStatusDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.service.PermitService;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.RouteSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.RouteSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
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

@WebMvcTest(PermitController.class)
@Import(TestSecurityConfiguration.class)
class PermitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PermitService permitService;

    private static final String API_URL = "/permits";


    // =========================================================
    // GET /permits
    // =========================================================

    @Test
    void getPermits_shouldReturn200_whenAuthorized() throws Exception {

        PermitDetailResponseDto response =
                PermitDetailResponseDto.builder()
                        .id(1)
                        .number("A12345")
                        .doissued(LocalDate.now().minusMonths(2))
                        .doexpired(LocalDate.now().plusMonths(10))
                        .build();

        when(permitService.getPermits())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-view")))
                .andExpect(status().isOk());

        verify(permitService).getPermits();
    }


    @Test
    void getPermits_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(permitService);
    }


    @Test
    void getPermits_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(permitService);
    }


    @Test
    void searchPermits_shouldCallSearchService_whenQueryParamsProvided() throws Exception {

        PermitDetailResponseDto response =
                PermitDetailResponseDto.builder()
                        .id(1)
                        .number("A12345")
                        .build();

        when(permitService.searchPermit(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("number", "A12345")
                        .with(user("test-user")
                                .authorities(() -> "permit-view")))
                .andExpect(status().isOk());

        verify(permitService).searchPermit(any());
        verify(permitService, never()).getPermits();
    }


    // =========================================================
    // GET /permits/summaries
    // =========================================================

    @Test
    void getSummaryPermits_shouldReturn200_whenAuthenticated() throws Exception {

        PermitSummaryResponseDto response =
                PermitSummaryResponseDto.builder()
                        .id(1)
                        .build();

        when(permitService.getSummaryPermits())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL + "/summaries")
                        .with(user("test-user")))
                .andExpect(status().isOk());

        verify(permitService).getSummaryPermits();
    }


    @Test
    void getSummaryPermits_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL + "/summaries"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(permitService);
    }


    // =========================================================
    // POST /permits
    // =========================================================

    @Test
    void createPermit_shouldReturn200_whenRequestIsValid() throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        PermitDetailResponseDto response =
                PermitDetailResponseDto.builder()
                        .id(1)
                        .number("A12345")
                        .build();

        when(permitService.createPermit(any(PermitCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(permitService)
                .createPermit(any(PermitCreateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createPermit_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PermitCreateRequestDto> mutator
    ) throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(permitService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "number",
                        "Number is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setNumber(null)
                ),

                Arguments.of(
                        "vehicle",
                        "Vehicle is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setVehicle(null)
                ),

                Arguments.of(
                        "doissued",
                        "Date of issued is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setDoissued(null)
                ),

                Arguments.of(
                        "notripsperday",
                        "Trip count is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setNotripsperday(null)
                ),

                Arguments.of(
                        "permitestatus",
                        "Permit status is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setPermitestatus(null)
                ),

                Arguments.of(
                        "servicetype",
                        "Service type is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setServicetype(null)
                ),

                Arguments.of(
                        "route",
                        "Route is mandatory",
                        (Consumer<PermitCreateRequestDto>)
                                dto -> dto.setRoute(null)
                )
        );
    }


    // =========================================================
    // Permit Number Validation
    // =========================================================

    @ParameterizedTest
    @MethodSource("invalidPermitNumberProvider")
    void createPermit_shouldReturn400_whenPermitNumberIsInvalid(
            String invalidPermitNumber
    ) throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        dto.setNumber(invalidPermitNumber);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString("Invalid Permit Number"))));

        verifyNoInteractions(permitService);
    }


    static Stream<String> invalidPermitNumberProvider() {

        return Stream.of(
                "12345",
                "1234A",
                "B12345",
                "AA12345",
                "A1234",
                "A123456",
                "F1234",
                "F123456",
                "A1234A",
                "A123 45"
        );
    }


    // =========================================================
    // Date Validation
    // =========================================================

    @Test
    void createPermit_shouldReturn400_whenIssuedDateIsInFuture() throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        dto.setDoissued(LocalDate.now().plusDays(1));

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(
                                "Issued date cannot be in the future"
                        ))));

        verifyNoInteractions(permitService);
    }


    // =========================================================
    // Positive Validation
    // =========================================================

    @Test
    void createPermit_shouldReturn400_whenTripCountIsNotPositive() throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        dto.setNotripsperday(0);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(
                                "Negative values are not allowed"
                        ))));

        verifyNoInteractions(permitService);
    }


    @ParameterizedTest
    @MethodSource("invalidTripCountProvider")
    void createPermit_shouldReturn400_whenTripCountIsInvalid(
            Integer invalidTripCount
    ) throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        dto.setNotripsperday(invalidTripCount);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(
                                "Negative values are not allowed"
                        ))));

        verifyNoInteractions(permitService);
    }


    static Stream<Integer> invalidTripCountProvider() {

        return Stream.of(
                0,
                -1,
                -10
        );
    }


    // =========================================================
    // POST /permits - Security
    // =========================================================

    @Test
    void createPermit_shouldReturn403_whenWrongAuthority() throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "permit-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(permitService);
    }


    @Test
    void createPermit_shouldReturn401_whenAnonymous() throws Exception {

        PermitCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(permitService);
    }


    // =========================================================
    // PUT /permits/transfer/{permitId}
    // =========================================================

    @Test
    void transferPermit_shouldReturn200_whenAuthorized() throws Exception {

        PermitDetailResponseDto response =
                PermitDetailResponseDto.builder()
                        .id(1)
                        .number("A12345")
                        .build();

        when(permitService.transferPermit(1))
                .thenReturn(response);

        mockMvc.perform(put(API_URL + "/transfer/1")
                        .with(user("test-user")
                                .authorities(() -> "permit-transfer")))
                .andExpect(status().isCreated());

        verify(permitService).transferPermit(1);
    }


    @Test
    void transferPermit_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(put(API_URL + "/transfer/1")
                        .with(user("test-user")
                                .authorities(() -> "permit-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(permitService);
    }


    @Test
    void transferPermit_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(put(API_URL + "/transfer/1"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(permitService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private PermitCreateRequestDto validCreateRequest() {

        return PermitCreateRequestDto.builder()
                .number("A12345")
                .vehicle(
                        VehicleSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .doissued(LocalDate.now().minusMonths(2))
                .notripsperday(10)
                .permitestatus(
                        PermitStatusDto.builder()
                                .id(1)
                                .build()
                )
                .servicetype(
                        ServiceTypeDto.builder()
                                .id(1)
                                .build()
                )
                .route(
                        RouteSummaryRequestDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }
}
