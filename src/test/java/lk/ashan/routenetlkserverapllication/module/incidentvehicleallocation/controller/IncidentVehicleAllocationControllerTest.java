package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service.IncidentVehicleAllocationService;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentVehicleAllocationController.class)
@Import(TestSecurityConfiguration.class)
class IncidentVehicleAllocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IncidentVehicleAllocationService allocationService;

    private static final String API_URL = "/incident-vehicle-allocations";


    // =========================================================
    // GET /incident-vehicle-allocations
    // =========================================================

    @Test
    void getAllocations_shouldReturn200_whenAuthorized() throws Exception {

        IncidentVehicleAllocationDetailsResponseDto response =
                validResponse();

        when(allocationService.getIncidentVehicleAllocations())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-view")))
                .andExpect(status().isOk());

        verify(allocationService).getIncidentVehicleAllocations();
    }


    @Test
    void getAllocations_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(allocationService);
    }


    @Test
    void getAllocations_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(allocationService);
    }


    @Test
    void searchAllocations_shouldCallSearchService_whenQueryParamsProvided()
            throws Exception {

        IncidentVehicleAllocationDetailsResponseDto response =
                validResponse();

        when(allocationService.searchIncidentAllocations(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("incident", "1")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-view")))
                .andExpect(status().isOk());

        verify(allocationService).searchIncidentAllocations(any());
        verify(allocationService, never())
                .getIncidentVehicleAllocations();
    }


    // =========================================================
    // POST /incident-vehicle-allocations
    // =========================================================

    @Test
    void createAllocation_shouldReturn201_whenRequestIsValid()
            throws Exception {

        IncidentVehicleAllocationCreateRequestDto dto =
                validCreateRequest();

        IncidentVehicleAllocationDetailsResponseDto response =
                validResponse();

        when(allocationService.createAllocation(
                any(IncidentVehicleAllocationCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(allocationService).createAllocation(
                any(IncidentVehicleAllocationCreateRequestDto.class)
        );
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createAllocation_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<IncidentVehicleAllocationCreateRequestDto> mutator
    ) throws Exception {

        IncidentVehicleAllocationCreateRequestDto dto =
                validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(allocationService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "incident",
                        "Incident is mandatory",
                        (Consumer<IncidentVehicleAllocationCreateRequestDto>)
                                dto -> dto.setIncident(null)
                ),

                Arguments.of(
                        "vehicle",
                        "Vehicle is mandatory",
                        (Consumer<IncidentVehicleAllocationCreateRequestDto>)
                                dto -> dto.setVehicle(null)
                ),

                Arguments.of(
                        "providedbranch",
                        "Provided Branch is mandatory",
                        (Consumer<IncidentVehicleAllocationCreateRequestDto>)
                                dto -> dto.setProvidedbranch(null)
                ),

                Arguments.of(
                        "incidentvehicleallocationstatus",
                        "Status is mandatory",
                        (Consumer<IncidentVehicleAllocationCreateRequestDto>)
                                dto -> dto.setIncidentvehicleallocationstatus(null)
                )
        );
    }


    @Test
    void createAllocation_shouldReturn403_whenWrongAuthority()
            throws Exception {

        IncidentVehicleAllocationCreateRequestDto dto =
                validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(allocationService);
    }


    @Test
    void createAllocation_shouldReturn401_whenAnonymous()
            throws Exception {

        IncidentVehicleAllocationCreateRequestDto dto =
                validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(allocationService);
    }


    // =========================================================
    // POST /incident-vehicle-allocations/{id}/in-progress
    // =========================================================

    @Test
    void inProgress_shouldReturn200_whenAuthorized()
            throws Exception {

        when(allocationService.inProgress(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/in-progress")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-in-progress")))
                .andExpect(status().isOk());

        verify(allocationService).inProgress(1);
    }


    @Test
    void inProgress_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/in-progress")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(allocationService);
    }


    @Test
    void inProgress_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/in-progress"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(allocationService);
    }


    // =========================================================
    // POST /incident-vehicle-allocations/{id}/released
    // =========================================================

    @Test
    void released_shouldReturn200_whenAuthorized()
            throws Exception {

        when(allocationService.released(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/released")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-released")))
                .andExpect(status().isOk());

        verify(allocationService).released(1);
    }


    @Test
    void released_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/released")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(allocationService);
    }


    @Test
    void released_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/released"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(allocationService);
    }


    // =========================================================
    // POST /incident-vehicle-allocations/{id}/cancelled
    // =========================================================

    @Test
    void cancelled_shouldReturn200_whenAuthorized()
            throws Exception {

        when(allocationService.cancelled(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/cancelled")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-cancelled")))
                .andExpect(status().isOk());

        verify(allocationService).cancelled(1);
    }


    @Test
    void cancelled_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/cancelled")
                        .with(user("test-user")
                                .authorities(() ->
                                        "incident-vehicle-allocation-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(allocationService);
    }


    @Test
    void cancelled_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/cancelled"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(allocationService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private IncidentVehicleAllocationCreateRequestDto validCreateRequest() {

        return IncidentVehicleAllocationCreateRequestDto.builder()
                .incident(
                        IncidentSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .vehicle(
                        VehicleSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .providedbranch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .incidentvehicleallocationstatus(
                        IncidentVehicleAllocationStatusDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    private IncidentVehicleAllocationDetailsResponseDto validResponse() {

        return IncidentVehicleAllocationDetailsResponseDto.builder()
                .id(1)
                .incident(
                        IncidentSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .vehicle(
                        VehicleSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .providedbranch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .incidentvehicleallocationstatus(
                        IncidentVehicleAllocationStatusDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }
}
